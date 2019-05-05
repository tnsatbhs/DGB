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
 
	}

}
