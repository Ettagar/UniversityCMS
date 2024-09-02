package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.List;

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

import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.CourseMapper;
import ua.foxminded.universitycms.mapper.ScheduleMapper;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.service.LessonTypeService;
import ua.foxminded.universitycms.service.RoleService;
import ua.foxminded.universitycms.service.ScheduleService;
import ua.foxminded.universitycms.service.TeacherService;
import ua.foxminded.universitycms.util.ScheduleTestData;

@ActiveProfiles("test")
@WebMvcTest(ScheduleController.class)
@Import({ CourseMapper.class, ScheduleMapper.class, TeacherMapper.class, StudentMapper.class, ScheduleTestData.class })
class ScheduleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ScheduleTestData scheduleTestData;

	@MockBean
	private CourseMapper courseMapper;

	@MockBean
	private ScheduleMapper scheduleMapper;

	@MockBean
	private TeacherMapper teacherMapper;

	@MockBean
	private ScheduleService scheduleService;

	@MockBean
	private LessonTypeService lessonTypeService;

	@MockBean
	private TeacherService teacherService;

	@MockBean
	private CourseService courseService;

	@MockBean
	private RoleService roleService;

	@MockBean
	private ScheduleTools scheduleTools;

	@MockBean
	private UserTools userTools;

	@MockBean
	private UserDetailsService userDetailsService;

	@BeforeEach
	void setup() {
		scheduleTestData.setUp();
	}

	@WithMockUser(username = "spring", roles = { "ADMIN" })
	@Test
	void testShowCreateForm() throws Exception {
		when(lessonTypeService.findAll()).thenReturn(List.of());

		mockMvc.perform(get("/schedules/schedule-create")).andExpect(status().isOk())
				.andExpect(view().name("schedules/schedule-create"))
				.andExpect(model().attributeExists("schedule", "lessonTypes"));
	}

	@WithMockUser(username = "spring", roles = { "ADMIN" })
	@Test
	void testListSchedules() throws Exception {
		LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
		LocalDateTime endDate = LocalDateTime.now().plusMonths(1);

		when(scheduleService.findSchedules(null, null, null, null, startDate, endDate)).thenReturn(List.of());
		when(scheduleMapper.toDto(List.of())).thenReturn(List.of());

		mockMvc.perform(get("/schedules")).andExpect(status().isOk()).andExpect(view().name("schedules/schedules"))
				.andExpect(model().attributeExists("schedules"));
	}

	@WithMockUser(username = "spring", roles = { "ADMIN" })
	@Test
	void testGetTeachersByCourse() throws Exception {
		LocalDateTime startDate = LocalDateTime.of(2024, 9, 1, 10, 0);
		LocalDateTime endDate = LocalDateTime.of(2024, 9, 1, 12, 0);
		Long courseId = 1L;

		when(scheduleTools.getTeachersDropdownData(startDate, endDate, courseId)).thenReturn(List.of());

		mockMvc.perform(get("/schedules/course/1/teachers").param("startDate", startDate.toString()).param("endDate",
				endDate.toString())).andExpect(status().isOk()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(0));
	}

	@WithMockUser(username = "spring", roles = { "ADMIN" })
	@Test
	void testGetClassrooms() throws Exception {
		LocalDateTime startDate = LocalDateTime.of(2024, 9, 1, 10, 0);
		LocalDateTime endDate = LocalDateTime.of(2024, 9, 1, 12, 0);
		Long scheduleId = 1L;

		when(scheduleTools.getClassroomsDropdownData(startDate, endDate, scheduleId)).thenReturn(List.of());

		mockMvc.perform(get("/schedules/getClassrooms").param("startDate", startDate.toString())
				.param("endDate", endDate.toString()).param("scheduleId", "1")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json")).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(0));
	}

	@WithMockUser(username = "spring", roles = { "ADMIN" })
	@Test
	void testViewScheduleNotFound() throws Exception {
		when(scheduleService.findById(1L)).thenThrow(new ServiceException("Schedule not found"));

		mockMvc.perform(get("/schedules/1")).andExpect(status().isNotFound()).andExpect(view().name("error/404"));
	}
}
