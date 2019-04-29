

import java.util.ArrayList;
import java.util.List;

import dgb.Gradebook;
import dgb.GradebookService;
import dgb.GradebookService.GradebookException;
import dgb.Student;
import junit.framework.TestCase;

public class GradebookServiceTest extends TestCase {

	public void testGetString() {
		//GradebookService gb = new GradebookService();
		//gb.getString("www.google.com");
		//System.out.println(gb);
		fail("Not yet implemented");
	}

	public void testSayPort() {
		fail("Not yet implemented");
	}

	public void testCreateGradebook() {
		GradebookService gb = new GradebookService();
		
		try {
			Gradebook b = gb.createGradebook("Math");
			assertEquals("Math", b.getName());
		}
		catch (Exception e) {
			
		}
	}

	public void testGetAllGradebooks() {
		GradebookService gb = new GradebookService();
		try {
			Gradebook b = gb.createGradebook("Math");
			List<Gradebook> bb = gb.getAllGradebooks();
			assertEquals("Math", bb);
		}
		catch (Exception e) {
			
		}
	}

	public void testGetAllStudents() {
			Gradebook gr = new Gradebook();
			Student s = new Student();
			gr.addStudent(s);
			
			ArrayList<Student> abc = (ArrayList<Student>) gr.getAllStudents();
			assertTrue(abc.contains(s));
	}

	public void testCreateStudent() {
		GradebookService gb = new GradebookService();
		try {
			gb.createStudent(123, "ashif", "A");
			
			Gradebook gr = new Gradebook();
			gr.setId(123);
			int v = gr.getId();
			assertEquals(123, v);
			
			Student st = new Student();
			
			st.setName("Ashif");
			String name = st.getName();
			assertEquals("Ashif", name);
			
			st.setGrade("A");
			String grade = st.getGrade();
			assertEquals("A", grade);
		}
		catch (GradebookException e) {
		}
		
		}
	}


