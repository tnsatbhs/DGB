

import junit.framework.TestCase;

import org.junit.Test;

import dgb.Student;

public class StudentTest extends TestCase {
	
	@Test
	public void testStudent() {
		Student st = new Student();
		assertNotNull(st);
	}
	
	@Test
	public void testSetName() {
		Student st = new Student();
		st.setName("Ashif");
		String name = st.getName();
		assertEquals("Ashif", name);
	}

	@Test
	public void testSetGrade() {
		Student st = new Student();
		st.setGrade("A");
		String grade = st.getGrade();
		assertEquals("A", grade);
	}

}
