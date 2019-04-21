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
	public void createStudent(@PathVariable String name, @PathVariable String grade)
	{
		//do something		
	}

}
