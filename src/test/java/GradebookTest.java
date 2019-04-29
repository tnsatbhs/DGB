

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
		Gradebook gr = new Gradebook();
		gr.setId(123);
		int v = gr.getId();
		assertEquals(123, v);
	}
	
	@Test
	public void testSetName() {
		Gradebook gr = new Gradebook();
		gr.setName("ashif");
		String v = gr.getName();
		assertEquals("ashif", v);
	}
	
	@Test
	public void testSetIsPrimaryServer() {
		Gradebook gr = new Gradebook();
		gr.setIsPrimaryServer(true);
		assertTrue(gr.getIsPrimaryServer());
	}

	@Test
	public void testGetAllStudents() {
		Gradebook gr = new Gradebook();
		Student s = new Student();
		gr.addStudent(s);
		
		ArrayList<Student> abc = (ArrayList<Student>) gr.getAllStudents();
		assertTrue(abc.contains(s));
	}

}
