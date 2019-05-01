package dgb;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GradebookController {

	Student student;
	//the above variables will not be used in the final code, they are just so return types can be satisfied in stubs


	@Autowired
	GradebookService gradebookService;



	@RequestMapping(path = "/gradebook/{name}", method = RequestMethod.POST,
			produces={"text/xml;charset=utf-8"})
	public ResponseEntity<Integer> createGradebook(@PathVariable String name)
	{
		System.out.println("Gradebook created successfully");

		Gradebook gradebook = createGradebookOp(name);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Location", "/gradebook/" + gradebook.getId());
		return new ResponseEntity<Integer>(gradebook.getId(), responseHeaders, HttpStatus.CREATED);
	}


	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.POST, produces={"text/xml;charset=utf-8"})
	public Gradebook createSecondary(@PathVariable Integer id)
	{
		//create secondary copy of gradebook, cannot be done on primary server
		//most of this will be done via logic and communication between apps
		try {
			System.out.println("TRYING TO CREATE A SECONDARY HERE...");
			Gradebook sec_gradebook = gradebookService.createSecondaryGradebook(id, Application.this_host, Application.secondary_host);
			System.out.println("Secondary copy of gradebook created successfully<");
			return sec_gradebook;
		} catch (GradebookNotFoundException e) {
			throw e;
		}
	}


	// Someone called a POST /secondary/{id} on the other server. That server called this server's
	// GET /gradebook/{id} to get the gradebook, create a secondary, and then called this endpoint to notify of a creation on the secondary.
	@RequestMapping(path = "/secondary/{id}/sync", method = RequestMethod.POST)
	public void syncSecondary(@PathVariable Integer id)
	{
		try {
			gradebookService.syncSecondaryGradebook(id, Application.secondary_host);
		} catch (GradebookNotFoundException e) {
			throw e;
		}
	}


	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.POST)
	public void createStudent(@PathVariable Integer id, @PathVariable String name,
			@PathVariable String grade)
	{
		//Von - add error checking here, throw exception if gradebook doesnt exist
		try {
			gradebookService.createStudent(id, name, grade);
		} catch (GradebookNotFoundException e) {
			throw e;
		} catch (InvalidGradeException e) {
			throw e;
		} catch (StudentExistsException e) {
			throw e;
		}
	}


	@RequestMapping(path = "/gradebook/{name}", method = RequestMethod.PUT)
	public int updateGradebook(@PathVariable String name, @RequestBody String newName)
	{
		return updateGradebookOp(name, newName);
	}


	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.PUT)
	public void updateSecondary(@PathVariable Integer id, @RequestBody Gradebook gradebook)
	{
		//create secondary copy of gradebook, cannot be done on primary server
		gradebookService.updateSecondaryGradebook(gradebook);
	}


	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.PUT)
	public void updateStudent(@PathVariable Integer id, @PathVariable String name,
			@PathVariable String grade)
	{
		try {
			gradebookService.updateStudent(id, name, grade);
		} catch (GradebookNotFoundException e) {
			throw e;
		} catch (InvalidGradeException e) {
			throw e;
		} catch (StudentNotFoundException e) {
			throw e;
		}
	}

	@RequestMapping(path = "/gradebook", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public Gradebooks getGradebooks()
	{
		//get all gradebooks on this server, including primary and secondary copies
		return gradebookService.getGradebooks();
	}


	@RequestMapping(path = "/gradebook/{id}", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public Gradebook getGradebook(@PathVariable Integer id)
	{
		//get all gradebooks on this server, including primary and secondary copies
		return gradebookService.getGradebookById(id);
	}


	@RequestMapping(path = "/gradebook/{id}/student", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public List<Student> getStudents(@PathVariable Integer id)
	{
		//Von - add error checking here, throw exception if gradebook doesnt exist
		try {
			return gradebookService.getStudents(id);
		} catch (GradebookNotFoundException e) {
			throw e;
		}
	}

	@RequestMapping(path = "/gradebook/{id}/student/{name}", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public Student getStudent(@PathVariable Integer id, @PathVariable String name)
	{
		//get student information, can be done on primary or secondary copy
		try {
			return gradebookService.getStudent(id, name);
		} catch (GradebookNotFoundException e) {
			throw e;
		} catch (StudentNotFoundException e) {
			throw e;
		}
	}

	@RequestMapping(path = "/gradebook/{id}", method = RequestMethod.DELETE)
	public void deleteGradebook(@PathVariable Integer id)
	{
		//delete gradebook, must be done on primary server, deletion also deletes secondary copies

		try {
			gradebookService.deleteGradebook(id);
		} catch (GradebookNotFoundException e) {
			throw e;
		}
	}

	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.DELETE)
	public void deleteSecondary(@PathVariable Integer id)
	{
		//delete secondary, must be done on secondary server, does not affect primary copy
		// gradebook := gradebookService.getGradebook(id);
		// if gradebook.isPrimary():
		//     throw error
		System.out.println("Deleting secondary: " + id.toString());
		gradebookService.deleteSecondaryGradebook(id);
	}

	@RequestMapping(path = "/gradebook/{id}/student/{name}", method = RequestMethod.DELETE)
	public void deleteStudent(@PathVariable Integer id, @PathVariable String name)
	{
		//delete gradebook, must be done on primary server, deletion auto updates secondary copies
		try {
			gradebookService.deleteStudent(id, name);
		} catch (GradebookNotFoundException e) {
			throw e;
		}
	}

	public Gradebook createGradebookOp(String name)
	{
		try {
			return gradebookService.createGradebook(name, true);
		} catch (GradebookExistsException e) {
			throw e;
		}
	}

	public int updateGradebookOp(String name, String newName) {
		try {
			return gradebookService.updateGradebook(name, newName);
		} catch (SecondaryEditNotAllowedException e) {
			throw e;
		}

	}


}
