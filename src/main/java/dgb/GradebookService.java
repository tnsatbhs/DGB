package dgb;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;



@Service
public class GradebookService extends RestTemplate {

	public static final String PROTOCOL = "http";


	@Autowired
	public GradebookRepository gradebookRepo;


	public void sayPort () {
		System.out.println("PORT: " + Application.ctx.getEnvironment().getProperty("local.server.port"));
	}


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
		for (Gradebook gradebook : gradebookRepo.findAll()) {
			if (gradebook.getName().equals(gradebookName)) {
				throw new GradebookExistsException("Gradebook Id-" + gradebook.getId());
			}
		}
		Gradebook gradebook = new Gradebook();
		gradebook.setName(gradebookName);
		gradebook.setIsPrimaryServer(isPrimary);
		this.saveGradebook(gradebook);
		return gradebook;
	}


	public void saveGradebook (Gradebook gradebook) {
		gradebookRepo.save(gradebook);
	}


	public void createSecondaryGradebook (Integer gradebookId, String secondaryHost) throws GradebookNotFoundException {
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();
		createSecondaryGradebook(gradebook, secondaryHost);
	}


	public void createSecondaryGradebook (Gradebook gradebook, String secondaryHost) {
		gradebook.setSecondaryHost(secondaryHost);
		gradebook.setIsPrimaryServer(false);
		try{
			this.postForLocation(PROTOCOL + "://" + gradebook.getSecondaryHost() + "/gradebook/" + gradebook.getId(), gradebook);
		}catch (RestClientException e){
			logger.error(e);
		}

		this.saveGradebook(gradebook);
	}

	public int updateGradebook(String gradebookName, String upName) {

		Gradebook oGradebook = gradebookRepo.findByName(gradebookName);

		if(!oGradebook.getIsPrimaryServer()){
			throw new SecondaryEditNotAllowedException("Gradebook Id-" + oGradebook.getId());
		}
		oGradebook.setName(upName);

		this.saveGradebook(oGradebook);
		return oGradebook.getId();

	}

	public void updateSecondaryGradebook (Gradebook gradebook) {
		this.postForLocation(PROTOCOL + "://" + gradebook.getSecondaryHost() + "/gradebook/" + gradebook.getId(), gradebook);
		this.saveGradebook(gradebook);
	}


	public List<Gradebook> getGradebooks () {
		return gradebookRepo.findAll();
	}


	public List<Student> getStudents (Integer gradebookId) {
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();
		return gradebook.getStudents();
	}


	// POST
	public void createStudent (Integer gradebookId, String studentName, String studentGrade) {
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		if (checkGradeInput(studentGrade)==false) throw new InvalidGradeException("grade-" + studentGrade);
		Gradebook gradebook = opt.get();
		if (!gradebook.getStudents().isEmpty()) {
			for (Student temp : gradebook.getStudents())
			{
				if (temp.getName().equals(studentName)) throw new StudentExistsException("grade-" + studentGrade); 
			}
		}
		Student student = new Student();
		student.setName(studentName);
		student.setGrade(studentGrade);
		gradebook.addStudent(student);
		this.saveGradebook(gradebook);
		// TODO: add student and grade, can not be done on secondary, changes must flow to secondary

		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null) {
			return;
		}

		// Save to secondary
		this.postForLocation(PROTOCOL + "://" + gradebook.getSecondaryHost() +
				"/gradebook/" + gradebook.getId() +
				"/student/" + studentName + "/grade/" + studentGrade,
				gradebook);

	}


	// PUT
	public void updateStudent (Integer gradebookId, String studentName, String studentGrade) throws GradebookNotFoundException, StudentNotFoundException {
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		if (checkGradeInput(studentGrade)==false) throw new InvalidGradeException("grade-" + studentGrade);
		Gradebook gradebook = opt.get();

		Student student = gradebook.getStudent(studentName);
		if (student == null) {
			throw new StudentNotFoundException("Name -" + studentName);
		}

		this.saveGradebook(gradebook);
		// TODO: add student and grade, can not be done on secondary, changes must flow to secondary

		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null) {
			return;
		}

		// Save to secondary
		this.put(PROTOCOL + "://" + secondaryHost +
				"/gradebook/" + gradebook.getId() +
				"/student/" + studentName +
				"/grade/" + studentGrade,
				gradebook);
	}
	
	public Student getStudent(Integer gradebookId, String studentName) throws GradebookNotFoundException, StudentNotFoundException
	{
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();

		Student student = gradebook.getStudent(studentName);
		if (student == null) {
			throw new StudentNotFoundException("Name -" + studentName);
		} else {
			return student;
		}
		
	}


	// DELETE
	public void deleteGradebook (Integer gradebookId) throws GradebookNotFoundException {
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();

		gradebookRepo.deleteById(gradebookId);

		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null) {
			return;
		}
		this.delete(PROTOCOL + "://" + secondaryHost + "/gradebook/" + gradebookId);
	}


	// DELETE
	public void deleteStudent (Integer gradebookId, String studentName) throws GradebookNotFoundException {
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();

		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null) {
			return;
		}
		this.delete(PROTOCOL + "://" + secondaryHost +
				"/gradebook/" + gradebookId +
				"/student/" + studentName);
	}
	
	public boolean checkGradeInput(String grade)
	{
		if (!grade.matches("[a-dA-D][+-]?|[eE]|[fF]|[iI]|[wW]|[zZ]")) return false;
		return true;
	}
}
