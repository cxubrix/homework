package fm.ask.kplavins.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fm.ask.kplavins.HomeworkApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HomeworkApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // dirty to keep tests in order
public class QuestionControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Test
    public void findById() throws Exception {
        this.mvc.perform(get("/questions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("Hello world?")))
                .andExpect(jsonPath("$.country", is("LV")))
                .andExpect(jsonPath("$.state", is("ACCEPTED")));
    }

    @Test
    public void getAllAccepted() throws Exception {
        this.mvc.perform(get("/questions"))
                .andExpect(status().isOk())                
                .andExpect(jsonPath("$", hasSize(4))); // before any POST req
    }

    @Test
    public void getAllAcceptedByCountry() throws Exception {
        this.mvc.perform(get("/questions?country=FR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].country", is("FR")));
    }
    
    @Test
    public void postQuestion_0_ok() throws Exception {
        this.mvc.perform(
                post("/questions")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", "This is new")
                )
                .andExpect(status().isOk())                
                .andExpect(jsonPath("$.content", is("This is new")));                
    }
    
    @Test
    public void postQuestion_1_blackist() throws Exception {
        this.mvc.perform(
                post("/questions")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", "This is new xxx")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("This is new xxx")));
    }
    
    @Before
    public void setUp() {        
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();        
    }
    

}
