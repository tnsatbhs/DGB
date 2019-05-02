import junit.framework.TestCase;

import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import dgb.Gradebook;
import dgb.Gradebooks;
import dgb.Student;

class ApplicationE2E extends TestCase {

	public static final String originA = "http://127.0.0.1:8080";
	public static final String originB = "http://127.0.0.1:8090";



	void clean() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = originA + "/gradebook";
			Gradebooks gradebooks = restTemplate.getForObject(url, Gradebooks.class);
			for (Gradebook gradebook : gradebooks) {
				if (gradebook.getName().equals("foo")) {
					restTemplate.delete(originA + "/gradebook/" + gradebook.getId());
					break;
				}
			}
		} catch (RestClientException exception) {
			System.err.println(exception.getMessage());
		} catch (Exception exception) {
			System.err.println(exception.getMessage());
		}
	}


	@Test
	void testReplicateWorkflow() {
		clean();


		RestTemplate restTemplate = new RestTemplate();
		try {
			HashMap<String, Student> studentsByName;
			Gradebook gradebook, gradebookReplica;
			Student student;
			String url;

			// Create on primary.
			url = originA + "/gradebook/foo";
			URI location = restTemplate.postForLocation(url, null);

			// Assert it is there.
			url = originA + location.getRawPath();
			gradebook = restTemplate.getForObject(url, Gradebook.class);
			assertEquals("foo", gradebook.getName());

			// Assert it is not on the secondary.
			url = originB + "/gradebook/" + gradebook.getId();
			final String url1 = url;
			assertThrows(RestClientException.class, () -> {
				restTemplate.getForObject(url1, Gradebook.class);
			}, "It should not be on the secondary");

			// Create a secondary replica.
			url = originB + "/secondary/" + gradebook.getId();
			gradebookReplica = restTemplate.postForObject(url, null, Gradebook.class);
			assertEquals(gradebook.getName(), gradebookReplica.getName());
			assertEquals(gradebook.getId(), gradebookReplica.getId());

			// Assert it is on the secondary.
			url = originB + "/gradebook/" + gradebook.getId();
			gradebookReplica = restTemplate.getForObject(url1, Gradebook.class);
			assertEquals(gradebook.getName(), gradebookReplica.getName());
			assertEquals(gradebook.getId(), gradebookReplica.getId());

			// Add students to the primary.
			url = originA + "/gradebook/" + gradebook.getId() + "/student/ALEX/grade/B+";
			gradebook = restTemplate.postForObject(url, null, Gradebook.class);
			studentsByName = new HashMap<String, Student>();
			for (Student s : gradebook.getStudents()) {
				studentsByName.put(s.getName(), s);
			}
			student = studentsByName.get("ALEX");
			assertTrue(student != null);

			// Ensure the secondary also got the student.
			url = originB + "/gradebook/" + gradebook.getId();
			gradebook = restTemplate.getForObject(url, Gradebook.class);
			studentsByName = new HashMap<String, Student>();
			for (Student s : gradebook.getStudents()) {
				studentsByName.put(s.getName(), s);
			}
			student = studentsByName.get("ALEX");
			assertTrue(student != null);

			// Try to update ALEX's grade
			url = originA + "/gradebook/" + gradebook.getId() + "/student/ALEX/grade/A-";
			restTemplate.put(url, null);

			// Ensure the grade was updated on the primary.
			url = originA + "/gradebook/" + gradebook.getId() + "/student/ALEX";
			student = restTemplate.getForObject(url, Student.class);
			assertEquals("A-", student.getGrade());
			assertEquals("ALEX", student.getName());

			// Ensure the grade was updated on the secondary.
			url = originB + "/gradebook/" + gradebook.getId() + "/student/ALEX";
			student = restTemplate.getForObject(url, Student.class);
			assertEquals("A-", student.getGrade());
			assertEquals("ALEX", student.getName());

		} catch (RestClientException exception) {
			System.err.println(exception.getMessage());
			fail(exception.getMessage());
		}

	}

}
