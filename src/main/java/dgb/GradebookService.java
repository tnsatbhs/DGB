package dgb;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GradebookService {
	
	public String getString (String entityUrl) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(entityUrl, String.class);
	}
	
}