import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

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
	public void testCreateStudent() {
		GradebookService svc = new GradebookService();
		try {
			svc.createStudent(123, "ashif", "A");

			Gradebook gradebook = new Gradebook();
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


