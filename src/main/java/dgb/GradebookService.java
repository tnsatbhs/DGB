package dgb;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class GradebookService {

	public class GradebookException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	@Autowired
	public GradebookRepository gradebookRepo;


	public String getString (String entityUrl) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(entityUrl, String.class);
	}

	public void sayPort () {
		System.out.println("PORT: " + Application.ctx.getEnvironment().getProperty("local.server.port"));
	}


	public Gradebook createGradebook (String gradebookName) throws Exception {
		Gradebook gradebook = new Gradebook();
		gradebook.setName(gradebookName);
		gradebook.setIsPrimaryServer(true);
		gradebookRepo.save(gradebook);
		return gradebook;
	}


	public List<Gradebook> getAllGradebooks () {
		return gradebookRepo.findAll();
	}


	public List<Student> getAllStudents (Integer gradebookId) throws GradebookException {
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookService.GradebookException();
		}
		Gradebook gradebook = opt.get();
		return gradebook.getAllStudents();
	}

	public void createStudent (Integer gradebookId, String studentName, String studentGrade) throws GradebookException {
		Optional<Gradebook> opt = gradebookRepo.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookService.GradebookException();
		}
		Gradebook gradebook = opt.get();
		Student student = new Student();
		student.setName(studentName);
		student.setGrade(studentGrade);
		gradebook.addStudent(student);
		gradebookRepo.save(gradebook);
		// TODO: add student and grade, can not be done on secondary, changes must flow to secondary
	}

}