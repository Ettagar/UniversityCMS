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
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.universitycms.mapper.CourseMapper;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.service.RoleService;
import ua.foxminded.universitycms.service.StudentService;
import ua.foxminded.universitycms.service.TeacherService;
import ua.foxminded.universitycms.service.UserService;
import ua.foxminded.universitycms.util.CourseTestData;

@ActiveProfiles("test")
@WebMvcTest(CourseController.class)
@Import(CourseMapper.class)
class CourseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CourseMapper courseMapper;

	@MockBean
	private CourseService courseService;

	@MockBean
	private StudentService studentService;

	@MockBean
	private StudentMapper studentMapper;

	@MockBean
	private TeacherService teacherService;

	@MockBean
	private TeacherMapper teacherMapper;

	@MockBean
	private UserService userService;

	@MockBean
	private RoleService roleService;

	@MockBean
	private UserTools userTools;
	
	@MockBean
    private UserDetailsService userDetailsService;

	private CourseTestData courseTestData;

	@BeforeEach
	void setup() {
		courseTestData = new CourseTestData();
		courseTestData.setUp();
	}

	@WithMockUser(username = "spring", roles = {"ADMIN"})
	@Test
	void testListCourses() throws Exception {
		when(courseService.findAll()).thenReturn(courseTestData.courses);

		mockMvc.perform(get("/courses"))
				.andExpect(status().isOk())
				.andExpect(view().name("courses/courses"))
				.andExpect(model().attributeExists("courses"))
				.andExpect(model().attribute("courses", courseMapper.toDto(courseTestData.courses)));
	}

	@WithMockUser(username = "spring", roles = {"ADMIN"}	)
	@Test
	void testViewCourse() throws Exception {
		when(courseService.findById(1L)).thenReturn(courseTestData.courses.get(0));

		mockMvc.perform(get("/courses/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("courses/course-detail"))
				.andExpect(model().attributeExists("course"))
				.andExpect(model().attribute("course", courseMapper.toDto(courseTestData.courses.get(0))));
	}
}
