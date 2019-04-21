import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


public class GradebookController {
	
	@RequestMapping(path = "/gradebook/{name}", method = RequestMethod.POST)
	public void createGradebook(@PathVariable String name)
	{
		//do something	
	}
	
	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.POST)
	public void createSecondary(@PathVariable Long id)
	{
		//do something		
	}
	
	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.POST)
	public void createStudent(@PathVariable Long id, @PathVariable String name,
			@PathVariable String grade)
	{
		//do something		
	}
	
	@RequestMapping(path = "/gradebook/{name}", method = RequestMethod.PUT)
	public void updateGradebook(@PathVariable String name)
	{
		//do something	
	}
	
	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.PUT)
	public void updateSecondary(@PathVariable Long id)
	{
		//do something		
	}
	
	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.PUT)
	public void updateStudent(@PathVariable Long id, @PathVariable String name,
			@PathVariable String grade)
	{
		//do something		
	}

}
