import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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
		//create the primary gradebook, check to make sure name is not already used
		return id;
	}
	
	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.POST)
	public void createSecondary(@PathVariable Long id)
	{
		//create secondary copy of gradebook, cannot be done on primary server		
	}
	
	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.POST)
	public void createStudent(@PathVariable Long id, @PathVariable String name,
			@PathVariable String grade)
	{
		//add student and grade, can not be done on secondary, changes must flow to secondary		
	}
	
	@RequestMapping(path = "/gradebook/{name}", method = RequestMethod.PUT)
	public Integer updateGradebook(@PathVariable String name)
	{
		//create/update the primary gradebook, check to make sure name is not already used
		return id;
	}
	
	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.PUT)
	public void updateSecondary(@PathVariable Long id)
	{
		//create secondary copy of gradebook, cannot be done on primary server	
	}
	
	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.PUT)
	public void updateStudent(@PathVariable Long id, @PathVariable String name,
			@PathVariable String grade)
	{
		//add student and grade, can not be done on secondary, changes must flow to secondary		
	}
	
	@RequestMapping(path = "/gradebook", method = RequestMethod.GET)
	public List<Gradebook> getAllGradebooks()
	{
		//get all gradebooks on this server, including primary and secondary copies
		return gradebookRepo.findAll();
	}
	
	@RequestMapping(path = "/gradebook/{id}/student", method = RequestMethod.GET)
	public ArrayList<Student> getAllStudents(@PathVariable Long id)
	{
		//get all students from gradebook, can be done on primary or secondary copy
		return students;
	}
	
	@RequestMapping(path = "/gradebook/{id}/student/{name}", method = RequestMethod.GET)
	public Student getStudent()
	{
		//get student information, can be done on primary or secondary copy
		return student;
	}
	
	@RequestMapping(path = "/gradebook/{id}", method = RequestMethod.DELETE)
	public void deleteGradebook(@PathVariable Long id)
	{
		//delete gradebook, must be done on primary server, deletion also deletes secondary copies		
	}
	
	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.DELETE)
	public void deleteSecondary(@PathVariable Long id)
	{
		//delete secondary, must be done on secondary server, does not affect primary copy		
	}
	
	@RequestMapping(path = "/gradebook/{id}/student/{name}", method = RequestMethod.DELETE)
	public void deleteStudent(@PathVariable Long id, @PathVariable String name)
	{
		//delete gradebook, must be done on primary server, deletion auto updates secondary copies	
	}

}
