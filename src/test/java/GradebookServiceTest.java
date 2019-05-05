import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import dgb.Gradebook;
import dgb.GradebookService;
import dgb.Student;

public class GradebookServiceTest extends TestCase {


	@Test
	public void testCreateGradebook() {
		GradebookService svc = new GradebookService();

		try {
			Gradebook gradebook = svc.createGradebook("Math", true);
			assertEquals("Math", gradebook.getName());
			assertTrue(gradebook.getIsPrimaryServer());
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void testThatTheCreatedGradebookHasPrimaryServer() {
		GradebookService svc = new GradebookService();

		try {
			Gradebook gradebook = svc.createSecondaryGradebook(12344, "primary" , "secondary");
			assertEquals("secondary" , gradebook.getSecondaryHost());
			assertFalse(gradebook.getIsPrimaryServer());
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void testGetGradebooks() {
		GradebookService svc = new GradebookService();
		try {
			svc.createGradebook("Math", false);
			List<Gradebook> gradebooks = svc.getGradebooks();
			ArrayList<String> names = new ArrayList<String>();
			for (Gradebook gradebook : gradebooks) {
				names.add(gradebook.getName());
			}
			assertTrue(names.contains("Math"));
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}


	@Test
	public void testGetStudents() {
		Gradebook gradebook = new Gradebook();
		Student student = new Student();
		gradebook.addStudent(student);

		ArrayList<Student> students = (ArrayList<Student>) gradebook.getStudents();
		assertTrue(students.contains(student));
	}

	@Test
	public void testGetStudents2() {
		List <Student>  students = new ArrayList();
		Student s1 = new Student();
		s1.setName("Ashif");
		s1.setGrade("A");
		
		Student s2 = new Student();
		s2.setName("Ashif2");
		s2.setGrade("B");
		
		students.add(s1);
		students.add(s2);
		Assert.assertEquals(2, students.size());
	}

	@Test
	public void testCreateStudent() {
		GradebookService svc = new GradebookService();
		try {
			Gradebook gradebook = svc.createStudent(123, "ashif", "A");

			gradebook.setId(123);
			int id = gradebook.getId();
			assertEquals(123, id);

			Student st = new Student();
			st.setName("Ashif");
			String name = st.getName();
			assertEquals("Ashif", name);

			st.setGrade("A");
			String grade = st.getGrade();
			assertEquals("A", grade);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
}


