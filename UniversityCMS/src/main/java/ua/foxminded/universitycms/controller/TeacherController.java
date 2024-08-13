package ua.foxminded.universitycms.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.dto.TeacherDto;
import ua.foxminded.universitycms.service.TeacherService;

@Controller
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {
	private final TeacherService teacherService;
	private final TeacherMapper teacherMapper;

	@GetMapping
	public String listTeachers(Model model) {
		List<TeacherDto> teachers = teacherMapper.toDto(teacherService.findAll());
		teachers.sort(Comparator.comparing(TeacherDto::getUserId));
		model.addAttribute("teachers", teachers);
		return ("teachers/teachers");
	}

	@GetMapping("/{id}")
	public String viewTeacher(@PathVariable Long id, Model model, HttpServletResponse response) {
		try {
			Teacher teacher = teacherService.findById(id);
			model.addAttribute("teacher", teacher);
			return ("teachers/teacher-detail");
		} catch (ServiceException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			model.addAttribute("errorMessage", e.getMessage());
			return ("error/404");
		}
	}
}
