

import java.awt.List;
import java.util.ArrayList;

import javax.validation.constraints.Size;

import org.junit.Test;

import dgb.Gradebook;
import dgb.Student;
import junit.framework.TestCase;

public class GradebookTest extends TestCase {

	@Test
	public void testSetId() {
		Gradebook gradebook = new Gradebook();
		gradebook.setId(123);
		int id = gradebook.getId();
		assertEquals(123, id);
	}

	@Test
	public void testSetName() {
		Gradebook gradebook = new Gradebook();
		gradebook.setName("ashif");
		String name = gradebook.getName();
		assertEquals("ashif", name);
	}

	@Test
	public void testSetIsPrimaryServer() {
		Gradebook gradebook = new Gradebook();
		gradebook.setIsPrimaryServer(true);
		assertTrue(gradebook.getIsPrimaryServer());
	}

	@Test
	public void testGetAllStudents() {
		Gradebook gradebook = new Gradebook();
		Student student = new Student();
		gradebook.addStudent(student);

		ArrayList<Student> students = (ArrayList<Student>) gradebook.getStudents();
		assertTrue(students.contains(student));
	}

}
