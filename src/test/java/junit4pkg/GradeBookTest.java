package junit4pkg;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dgb.Gradebook;
import dgb.Student;

public class GradeBookTest {
	
	private List <Student>  students;
	
	@Before
	public void setUp() throws Exception {
		students = new ArrayList();
		Student s1 = new Student();
		s1.setName("Ashif");
		s1.setGrade("A");
		
		Student s2 = new Student();
		s2.setName("Ashif2");
		s2.setGrade("B");
		
		students.add(s1);
		students.add(s2);
	}

	@After
	public void tearDown() throws Exception {
		students = null;
	}

	@Test
	public void testGetSetId() {
		Gradebook gr = new Gradebook();
		gr.setId(123);
		int v = gr.getId();
		assertEquals(123, v);
	}

	@Test
	public void testGetSetName() {
		Gradebook gr = new Gradebook();
		gr.setName("ashif");
		String v = gr.getName();
		assertEquals("ashif", v);
	}

	@Test
	public void testGetSetIsPrimaryServer() {
		Gradebook gr = new Gradebook();
		gr.setIsPrimaryServer(true);
		assertTrue(gr.getIsPrimaryServer());
	}

	@Test
	public void testGetStudents() {
		Assert.assertEquals(2, this.students.size());
	}

	@Test
	public void testAddStudent() {
		Student s3 = new Student();
		s3.setName("Ashif3");
		s3.setGrade("B");
		students.add(s3);
		Assert.assertNotEquals(2, this.students.size());
		Assert.assertEquals(3, this.students.size());
	}

	@Test
	public void testRemoveStudent() {
		Student s3 = new Student();
		s3.setName("Ashif3");
		s3.setGrade("B");
		students.add(s3);
		
		students.remove(s3);
		
		Assert.assertNotEquals(3, this.students.size());
		Assert.assertEquals(2, this.students.size());
	}

	@Test
	public void testGetStudent() {
		Gradebook gr = new Gradebook();
		
		Student s3 = new Student();
			s3.setName("Ashif3");
			s3.setGrade("B");
	
			gr.addStudent(s3);

		Assert.assertEquals(s3, gr.getStudent("Ashif3, B"));
	}

	
	@Test
	public void testGetSetSecondaryHost() {
		Gradebook gr = new Gradebook();
		
		gr.setSecondaryHost("value.com");
		
		Assert.assertEquals("value.com", gr.getSecondaryHost());
	}

}
