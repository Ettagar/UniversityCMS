package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.util.CourseTestData;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private CourseTestData courseTestData;

    @BeforeEach
    void setup() {
		courseTestData = new CourseTestData();
		courseTestData.setUp();
	}

    @Test
    void testListCourses() throws Exception {
        when(courseService.findAll()).thenReturn(courseTestData.courses);

        mockMvc.perform(get("/courses"))
               .andExpect(status().isOk())
               .andExpect(view().name("courses/courses"))
               .andExpect(model().attributeExists("courses"))
               .andExpect(model().attribute("courses", courseTestData.courses));
    }

    @Test
    void testViewCourse() throws Exception {
        when(courseService.findById(1L)).thenReturn(courseTestData.courses.get(0));

        mockMvc.perform(get("/courses/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("courses/course-detail"))
               .andExpect(model().attributeExists("course"))
               .andExpect(model().attribute("course", courseTestData.courses.get(0)));
    }
}
