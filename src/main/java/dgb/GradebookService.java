package dgb;

import org.springframework.web.client.RestTemplate;


public class GradebookService {


	public String getString (String entityUrl) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(entityUrl, String.class);
	}

	public void sayPort () {
		System.out.println("PORT: " + Application.ctx.getEnvironment().getProperty("local.server.port"));
	}


	public Gradebook create (Gradebook gradebook) {
		return  gradebook;
	}

}