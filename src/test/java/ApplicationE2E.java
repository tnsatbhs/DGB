import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import dgb.Gradebook;
import dgb.Gradebooks;

class ApplicationE2E extends TestCase {

	public static final String originA = "http://127.0.0.1:8080";
	public static final String originB = "http://127.0.0.1:8090";



	void clean() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = originA + "/gradebook";
			Gradebooks gradebooks = restTemplate.getForObject(url, Gradebooks.class);
			for (Gradebook gradebook : gradebooks) {
				if (gradebook.getName() == "foo") {
					restTemplate.delete(originA + "/gradebook/" + gradebook.getId());
					break;
				}
			}
			assertTrue(true);
		} catch (RestClientException exception) {
			System.err.println(exception.getMessage());
		} catch (Exception exception) {
			System.err.println(exception.getMessage());
		}
	}


	@Test
	void testCreateGradebook() {
		clean();


		RestTemplate restTemplate = new RestTemplate();
		String url = originA + "/gradebook/foo";
		try {
			Gradebook gradebook = restTemplate.postForObject(url, null, Gradebook.class);
			assertTrue(gradebook.getId() != null);
			assertEquals("foo", gradebook.getName());
		} catch (RestClientException exception) {
			System.err.println(exception.getMessage());
			fail(exception.getMessage());
		}
	}

}
