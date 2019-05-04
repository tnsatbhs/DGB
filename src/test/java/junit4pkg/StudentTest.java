package junit4pkg;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dgb.Student;

public class StudentTest {
	
	private Student student;
	
	@Before
	public void setUp() throws Exception {
		student = new Student();
	}

	@After
	public void tearDown() throws Exception {
		student = null;
	}

	@Test
	public void testStudent() {
		Student s1 = null;
		assertNull(s1);
		assertNotNull(student);
	}

	@Test
	public void testGetSetName() {
		
		student.setName("Ashif");
		String name = student.getName();
		assertEquals("Ashif", name);
	}

	@Test
	public void testGetSetGrade() {
		student.setGrade("A");
		String grade = student.getGrade();
		assertEquals("A", grade);
	}

}
