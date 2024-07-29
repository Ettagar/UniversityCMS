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

import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.service.TeacherService;
import ua.foxminded.universitycms.util.TeacherTestData;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    private TeacherTestData teacherTestData;

    @BeforeEach
    void setup() {
		teacherTestData = new TeacherTestData();
		teacherTestData.setUp();
    }

    @Test
    void testListTeachers() throws Exception {
        when(teacherService.findAll()).thenReturn(teacherTestData.teachers);

        mockMvc.perform(get("/teachers"))
               .andExpect(status().isOk())
               .andExpect(view().name("teachers/teachers"))
               .andExpect(model().attributeExists("teachers"))
               .andExpect(model().attribute("teachers", teacherTestData.teachers));
    }

    @Test
    void testViewTeacher() throws Exception {
        when(teacherService.findById(1L)).thenReturn(teacherTestData.teachers.get(0));

        mockMvc.perform(get("/teachers/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("teachers/teacher-detail"))
               .andExpect(model().attributeExists("teacher"))
               .andExpect(model().attribute("teacher", teacherTestData.teachers.get(0)));
    }

    @Test
    void testViewTeacherNotFound() throws Exception {
        when(teacherService.findById(4L)).thenThrow(new ServiceException("Teacher not found"));

        mockMvc.perform(get("/teachers/4"))
               .andExpect(status().isNotFound())
               .andExpect(view().name("error/404"))
               .andExpect(model().attributeExists("errorMessage"))
               .andExpect(model().attribute("errorMessage", "Teacher not found"));
    }
}
