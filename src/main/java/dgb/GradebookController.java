package dgb;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GradebookController {
	
	Integer id;
	ArrayList<Student> students;
	Student student;
	//the above variables will not be used in the final code, they are just so return types can be satisfied in stubs
	
	@Autowired
	GradebookRepository gradebookRepo;
	
	@RequestMapping(path = "/gradebook/{name}", method = RequestMethod.POST)
	public Integer createGradebook(@PathVariable String name)
	{
		return createGradebookOp(name);
	}
	
	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.POST)
	public void createSecondary(@PathVariable Integer id)
	{
		//create secondary copy of gradebook, cannot be done on primary server
		//most of this will be done via logic and communication between apps
	}
	
	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.POST)
	public void createStudent(@PathVariable Integer id, @PathVariable String name,
			@PathVariable String grade)
	{
		//Von - add error checking here, throw exception if gradebook doesnt exist
		Optional<Gradebook> opt = gradebookRepo.findById(id);
		Gradebook tempG = opt.get();
		Student tempS = new Student();
		tempS.setName(name);
		tempS.setGrade(grade);
		tempG.addStudent(tempS);
		gradebookRepo.save(tempG);
		//add student and grade, can not be done on secondary, changes must flow to secondary		
	}
	
	@RequestMapping(path = "/gradebook/{name}", method = RequestMethod.PUT)
	public Integer updateGradebook(@PathVariable String name)
	{
		return createGradebookOp(name);
	}
	
	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.PUT)
	public void updateSecondary(@PathVariable Integer id)
	{
		//create secondary copy of gradebook, cannot be done on primary server
		//most of this will be done via logic and communication between apps
	}
	
	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.PUT)
	public void updateStudent(@PathVariable Integer id, @PathVariable String name,
			@PathVariable String grade)
	{
		//add student and grade, can not be done on secondary, changes must flow to secondary		
	}
	
	@RequestMapping(path = "/gradebook", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public List<Gradebook> getAllGradebooks()
	{
		//get all gradebooks on this server, including primary and secondary copies
		return gradebookRepo.findAll();
	}
	
	@RequestMapping(path = "/gradebook/{id}/student", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public List<Student> getAllStudents(@PathVariable Integer id)
	{
		//Von - add error checking here, throw exception if gradebook doesnt exist
		Optional<Gradebook> tempG = gradebookRepo.findById(id);
		return tempG.get().getAllStudents();
		//get all students from gradebook, can be done on primary or secondary copy
	}
	
	@RequestMapping(path = "/gradebook/{id}/student/{name}", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public Student getStudent(@PathVariable Integer id, @PathVariable String name)
	{
		//get student information, can be done on primary or secondary copy
		return student;
	}
	
	@RequestMapping(path = "/gradebook/{id}", method = RequestMethod.DELETE)
	public void deleteGradebook(@PathVariable Integer id)
	{
		//delete gradebook, must be done on primary server, deletion also deletes secondary copies		
	}
	
	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.DELETE)
	public void deleteSecondary(@PathVariable Integer id)
	{
		//delete secondary, must be done on secondary server, does not affect primary copy		
	}
	
	@RequestMapping(path = "/gradebook/{id}/student/{name}", method = RequestMethod.DELETE)
	public void deleteStudent(@PathVariable Integer id, @PathVariable String name)
	{
		//delete gradebook, must be done on primary server, deletion auto updates secondary copies	
	}
	
	public Integer createGradebookOp(String name)
	{
		Gradebook gradebook = new Gradebook();
		gradebook.setName(name);
		gradebookRepo.save(gradebook);
		return gradebook.getId();	
	}

}
