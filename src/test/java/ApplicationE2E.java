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

			final Integer gradebookId = gradebook.getId();
			// Assert it is not on the secondary.
			assertThrows(RestClientException.class, () -> {
				String _url = originB + "/gradebook/" + gradebookId;
				restTemplate.getForObject(_url, Gradebook.class);
			}, "It should not be on the secondary");

			// Create a secondary replica.
			url = originB + "/secondary/" + gradebookId;
			gradebookReplica = restTemplate.postForObject(url, null, Gradebook.class);
			assertEquals(gradebook.getName(), gradebookReplica.getName());
			assertEquals(gradebookId, gradebookReplica.getId());

			// Assert it is on the secondary.
			url = originB + "/gradebook/" + gradebookId;
			gradebookReplica = restTemplate.getForObject(url, Gradebook.class);
			assertEquals(gradebook.getName(), gradebookReplica.getName());
			assertEquals(gradebookId, gradebookReplica.getId());

			// Add students to the primary.
			url = originA + "/gradebook/" + gradebookId + "/student/ALEX/grade/B+";
			gradebook = restTemplate.postForObject(url, null, Gradebook.class);
			studentsByName = new HashMap<String, Student>();
			for (Student s : gradebook.getStudents()) {
				studentsByName.put(s.getName(), s);
			}
			student = studentsByName.get("ALEX");
			assertTrue(student != null);

			// Ensure the secondary also got the student.
			url = originB + "/gradebook/" + gradebookId;
			gradebook = restTemplate.getForObject(url, Gradebook.class);
			studentsByName = new HashMap<String, Student>();
			for (Student s : gradebook.getStudents()) {
				studentsByName.put(s.getName(), s);
			}
			student = studentsByName.get("ALEX");
			assertTrue(student != null);

			// Try to update ALEX's grade
			url = originA + "/gradebook/" + gradebookId + "/student/ALEX/grade/A-";
			restTemplate.put(url, null);

			// Ensure the grade was updated on the primary.
			url = originA + "/gradebook/" + gradebookId + "/student/ALEX";
			student = restTemplate.getForObject(url, Student.class);
			assertEquals("A-", student.getGrade());
			assertEquals("ALEX", student.getName());

			// Ensure the grade was updated on the secondary.
			url = originB + "/gradebook/" + gradebookId + "/student/ALEX";
			student = restTemplate.getForObject(url, Student.class);
			assertEquals("A-", student.getGrade());
			assertEquals("ALEX", student.getName());

			// You should be unable to edit the grade on the secondary.
			assertThrows(RestClientException.class, () -> {
				String _url = originB + "/gradebook/" + gradebookId + "/student/ALEX/grade/C+";
				restTemplate.put(_url, null);
			}, "You should not be able to PUT a grade on the secondary");

			// You should be unable to add a student / grade on the secondary.
			assertThrows(RestClientException.class, () -> {
				String _url = originB + "/gradebook/" + gradebookId + "/student/XELA/grade/A+";
				restTemplate.postForLocation(_url, null);
			}, "You should not be able to POST a grade on the secondary");

			// You should be unable to delete a student on the secondary.
			assertThrows(RestClientException.class, () -> {
				String _url = originB + "/gradebook/" + gradebookId + "/student/ALEX";
				restTemplate.delete(_url);
			}, "You should not be able to DELETE a grade on the secondary");

			// Prevent PUT on secondary/id
			assertThrows(RestClientException.class, () -> {
				String _url = originB + "/gradebook/foo";
				restTemplate.put(_url, null);
			}, "You should not be able to PUT the same name on the secondary");

			// Prevent POST on secondary/id
			assertThrows(RestClientException.class, () -> {
				String _url = originB + "/gradebook/foo";
				restTemplate.put(_url, null);
			}, "You should not be able to POST the same name on the secondary");

			// Prevent POST on primary for secondary/id api
			assertThrows(RestClientException.class, () -> {
				String _url = originA + "/secondary/" + gradebookId;
				restTemplate.postForLocation(_url, null);
			}, "You should not be able to run POST on this Api on primary");

			// Prevent PUT on primary for secondary/id api
			assertThrows(RestClientException.class, () -> {
				String _url = originA + "/secondary/" + gradebookId;
				restTemplate.put(_url, null);
			}, "You should not be able to run PUT on this Api on primary");

			// Prevent Delete on primary for secondary/id api
			assertThrows(RestClientException.class, () -> {
				String _url = originA + "/secondary/" + gradebookId;
				restTemplate.delete(_url);
			}, "You should not be able to run Delete on this Api on primary");

			// Prevent POST on secondary/id
			assertThrows(RestClientException.class, () -> {
				String _url = originB + "/gradebook/foo";
				restTemplate.postForLocation(_url, null);
			}, "You should not be able to POST the same name on the secondary");

			// Prevent DELETE on secondary/id
			assertThrows(RestClientException.class, () -> {
				String _url = originB + "/gradebook/foo";
				restTemplate.delete(_url);
			}, "You should not be able to DELETE the same name on the secondary");


		} catch (RestClientException exception) {
			System.err.println(exception.getMessage());
			fail(exception.getMessage());
		}

	}

}
