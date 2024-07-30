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
import ua.foxminded.universitycms.service.StudentService;
import ua.foxminded.universitycms.util.StudentTestData;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StudentService studentService;

	private StudentTestData studentTestData;

	@BeforeEach
	void setup() {
		studentTestData = new StudentTestData();
		studentTestData.setUp();
	}

	@Test
	void testListStudents() throws Exception {
		when(studentService.findAll()).thenReturn(studentTestData.students);

		mockMvc.perform(get("/students")).andExpect(status().isOk()).andExpect(view().name("students/students"))
				.andExpect(model().attributeExists("students"))
				.andExpect(model().attribute("students", studentTestData.students));
	}

	@Test
	void testViewStudent() throws Exception {
		when(studentService.findById(1L)).thenReturn(studentTestData.students.get(0));

		mockMvc.perform(get("/students/1")).andExpect(status().isOk()).andExpect(view().name("students/student-detail"))
				.andExpect(model().attributeExists("student"))
				.andExpect(model().attribute("student", studentTestData.students.get(0)));
	}

	@Test
	void testViewStudentNotFound() throws Exception {
		when(studentService.findById(1L)).thenThrow(new ServiceException("Student not found"));

		mockMvc.perform(get("/students/1")).andExpect(status().isNotFound()).andExpect(view().name("error/404"));
	}
}
