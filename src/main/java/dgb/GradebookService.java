package dgb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;



@Service
public class GradebookService extends RestTemplate {

	public static final String PROTOCOL = "http";
	private Integer bookId = 1;


	@Autowired
	public GradebookRepository gradebookRepo;


	public Gradebook createGradebook (Gradebook gradebook, Boolean isPrimary) throws GradebookExistsException {

		for (Gradebook gb : gradebookRepo.findAll()) {
			if (gb.getName().equals(gradebook.getName())) {
				throw new GradebookExistsException("Gradebook Id-" + gradebook.getId());
			}
		}
		gradebook.setIsPrimaryServer(isPrimary);
		this.saveGradebook(gradebook);
		return gradebook;
	}


	public Gradebook createGradebook (String gradebookName, Boolean isPrimary) throws GradebookExistsException {
		for (Gradebook gb : gradebookRepo.findAll()) {
			if (gb.getName().equals(gradebookName)) {
				throw new GradebookExistsException("Gradebook Id-" + gb.getId());
			}
		}
		Gradebook gradebook = new Gradebook();
		gradebook.setName(gradebookName);
		gradebook.setId(bookId);
		bookId++;
		
		String url = PROTOCOL + "://" + Application.secondary_host +
				"/syncId/" + bookId;
		System.out.println("Trying to sync GradebookId: " + bookId);
		this.postForLocation(url, null);
		
		//flow to secondary
		
		return this.createGradebook(gradebook, isPrimary);
	}


	public void saveGradebook (Gradebook gradebook) {
		gradebookRepo.save(gradebook);
	}


	// This method is weird. The requirements expect the user to call this on the secondary host,
	// which means that we need to call the primary to get the gradebook in question. If it doesn't exist, then fail.
	// If it exists on this server, then fail.
	public Gradebook createSecondaryGradebook (Integer gradebookId, String thisHost, String primaryHost) throws GradebookNotFoundException {
		Gradebook gradebook;

		try {
			gradebook = getGradebookById(gradebookId);
			if (gradebook != null) {
				System.err.println("Found gradebook name " + gradebook.getName());
				throw new GradebookExistsException(gradebookId.toString() + " already exists on this server");
			}
		} catch (GradebookNotFoundException exception) {
			// Good...
		}

		// Get the gradebook from the primary.
		try {
			String uri = PROTOCOL + "://" + primaryHost +
					"/gradebook/" + gradebookId;
			System.out.println("Calling: " + uri);
			gradebook = this.getForObject(uri, Gradebook.class);
		} catch (RestClientException exception) {
			System.err.println("Failed to get the gradebook from the primary");

			// TODO: how do we figure if it's a 404?
			throw new GradebookNotFoundException("Not found");
		}

		// This is so freaking lame. We need to get the gradebook from the primary server, save it here, and then update
		// the primary server's copy so that it knows a secondary exists.

		// Add secondary host.
		// We are setting the secondary host to THIS server.
		gradebook.setSecondaryHost(thisHost);
		gradebook.setIsPrimaryServer(false);

		// Push to secondary.
		try {
			this.postForLocation(PROTOCOL + "://" + primaryHost +
					"/secondary/" + gradebookId + "/sync", null);
		} catch (RestClientException e){
			System.err.println("Failed to sync the primary");
			// I don't think we want to continue saving the secondary
			throw e;
		}
		// Assuming the above passed, we save the gradebook's edit.
		this.saveGradebook(gradebook);

		return gradebook;
	}


	public int updateGradebook(String gradebookName, String newName) {
		Gradebook gradebook = gradebookRepo.findByName(gradebookName);
		if(!gradebook.getIsPrimaryServer()){
			throw new SecondaryEditNotAllowedException("Gradebook Id-" + gradebook.getId());
		}
		gradebook.setName(newName);
		this.saveGradebook(gradebook);
		return gradebook.getId();
	}


	public void updateSecondaryGradebook (Gradebook gradebook) {
		this.postForLocation(PROTOCOL + "://" + gradebook.getSecondaryHost() + "/gradebook/" + gradebook.getId(), gradebook);
		this.saveGradebook(gradebook);
	}


	public Gradebooks getGradebooks () {
		Gradebooks gradebooks = new Gradebooks();
		for (Gradebook gradebook : gradebookRepo.findAll()) {
			gradebooks.add(gradebook);
		}
		return gradebooks;
	}


	public List<Student> getStudents (Integer gradebookId) {
		Gradebook gradebook = getGradebookById(gradebookId);
		return gradebook.getStudents();
	}


	// POST
	public Gradebook createStudent (Integer gradebookId, String studentName, String studentGrade) {
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		if (!isValidGrade(studentGrade)) {
			throw new InvalidGradeException("grade-" + studentGrade);
		}
		Gradebook gradebook = opt.get();
		ArrayList<Student> students = gradebook.getStudents();
		if (students == null) {
			// This gradebook was saved without any students, so create that object now.
			gradebook.setStudents(new ArrayList<Student>());
		} else if (!students.isEmpty()) {
			for (Student student : gradebook.getStudents()) {
				if (student.getName().equals(studentName)) {
					throw new StudentExistsException("grade-" + studentGrade);
				}
			}
		}
		Student student = new Student();
		student.setName(studentName);
		student.setGrade(studentGrade);
		gradebook.addStudent(student);
		System.out.println("Adding new student: " + student.getName() + " to " + Application.this_host);
		this.saveGradebook(gradebook);

		// Push new student to secondary host.
		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null || !gradebook.getIsPrimaryServer()) {
			// Don't push if there is no secondary or if this isn't the primary.
			return gradebook;
		}

		// Save to secondary
		try{
			String url = PROTOCOL + "://" + gradebook.getSecondaryHost() +
					"/gradebook/" + gradebook.getId() +
					"/student/" + studentName + "/grade/" + studentGrade;
			System.out.println("Trying to create secondary student: " + url);
			this.postForLocation(url, null);
		}catch (RestClientException exception) {
			exception.printStackTrace();
			System.err.println("Failed to Sync with secondary server");
			throw new SecondarySyncFailedException("Sync failed");
		}

		return gradebook;
	}


	// PUT
	public void updateStudent (Integer gradebookId, String studentName, String studentGrade) throws GradebookNotFoundException, StudentNotFoundException {
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();
		Student student = gradebook.getStudent(studentName);
		if (student == null) {
			throw new StudentNotFoundException("Name -" + studentName);
		}
		if (!isValidGrade(studentGrade)) {
			throw new InvalidGradeException("grade-" + studentGrade);
		}
		student.setGrade(studentGrade);
		this.saveGradebook(gradebook);
		// TODO: add student and grade, can not be done on secondary, changes must flow to secondary
		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null || !gradebook.getIsPrimaryServer()) {
			return;
		}

		// Save to secondary
		this.put(PROTOCOL + "://" + secondaryHost +
				"/gradebook/" + gradebook.getId() +
				"/student/" + studentName +
				"/grade/" + studentGrade,
				null);
	}


	public Student getStudent(Integer gradebookId, String studentName) throws GradebookNotFoundException, StudentNotFoundException {
		Gradebook gradebook = getGradebookById(gradebookId);
		Student student = gradebook.getStudent(studentName);
		if (student == null) {
			throw new StudentNotFoundException("Name -" + studentName);
		} else {
			return student;
		}
	}


	// DELETE
	public void deleteGradebook (Integer gradebookId) throws GradebookNotFoundException {
		Gradebook gradebook = getGradebookById(gradebookId);
		String secondaryHost = gradebook.getSecondaryHost();
		System.out.println("Deleted gradebook with id: " + gradebookId.toString());
		gradebookRepo.deleteById(gradebookId);
		if (secondaryHost == null) {
			System.out.println("No secondary host for gradebook: " + gradebookId.toString());
			return;
		}
		this.delete(PROTOCOL + "://" + secondaryHost + "/secondary/" + gradebookId);
	}


	// DELETE
	public void deleteSecondaryGradebook (Integer gradebookId) throws GradebookNotFoundException {
		Gradebook gradebook = getGradebookById(gradebookId);
		if (gradebook.getIsPrimaryServer()) {
			throw new SecondaryEditNotAllowedException("Something");
		}
		gradebookRepo.deleteById(gradebookId);
	}


	// DELETE
	public void deleteStudent (Integer gradebookId, String studentName) throws GradebookNotFoundException {
		Gradebook gradebook = getGradebookById(gradebookId);
		if (!gradebook.getIsPrimaryServer()) {
			throw new SecondaryEditNotAllowedException("Something");
		}
		gradebook.removeStudent(studentName);
		this.saveGradebook(gradebook);
		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null) {
			return;
		}
		this.delete(PROTOCOL + "://" + secondaryHost +
				"/gradebook/" + gradebookId +
				"/student/" + studentName);
	}


	public boolean isValidGrade(String grade) {
		if (grade.matches("[a-dA-D][+-]?|[eE]|[fF]|[iI]|[wW]|[zZ]")) {
			return true;
		}
		return false;
	}


	public Gradebook getGradebookById(int gradebookId){
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();
		return gradebook;
	}


	// Save the gradebook's secondary host. It was likely just created on the secondary.
	public void syncSecondaryGradebook(Integer gradebookId, String secondaryHost) {
		System.out.println("Syncing gradebook " + gradebookId + " with " + secondaryHost);
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}

		Gradebook gradebook = opt.get();
		gradebook.setSecondaryHost(secondaryHost);
		this.saveGradebook(gradebook);
	}
	
	public void syncId(Integer id)
	{
		this.bookId = id;
	}

}
