package junit4pkg;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dgb.Application;
import dgb.Gradebook;
import dgb.GradebookRepository;

import org.springframework.test.context.TestPropertySource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class DgbTest {

	/*
	 * 
	 *Every GradeBook has one primary server
	Every GradeBook has zero or one secondary server
	A GradeBook's primary server cannot be a secondary server for that GradeBook
	GradeBook titles are unique across the DGB system
	GradeBook IDs are unique across the DGB system
	Every GradeBook must have a title, which must be a character string that begins with a non-whitespace character.
	GradeBook updates must be done through the primary server
	The primary server is responsible for propagating updates to secondary servers, if any
	A GradeBook can be empty (that is, contain no students)
	For every student, the content must be non-empty
	Student names are unique across a GradeBook
	 */
	
	@Autowired
    private MockMvc mvc;
 
    @Autowired
    private GradebookRepository repository;
	
    //GradeBook IDs are unique across the DGB system
	@Test
	public void test() throws Exception {
		
		mvc.perform(post("/gradebook/{title}", "cs"))
		
	            .andExpect(status().isOk());

	    Gradebook gb2 = repository.findById(1).get();	
	   
	    assertEquals("cs",gb2.getName());
 
	 //
	    
	    
	    
		 
		/*
		 * mvc.perform(get("/api/employees") .contentType(MediaType.APPLICATION_JSON))
		 * .andExpect(status().isOk()) .andExpect(content()
		 * .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		 * .andExpect(jsonPath("$[0].name", is("bob")));
		 */
		
	}
	
	
	
	

}
