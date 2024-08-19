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
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.dto.StudentDto;
import ua.foxminded.universitycms.service.StudentService;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
	private final StudentService studentService;
	private final StudentMapper studentMapper;

	@GetMapping
	public String listStudents(Model model) {
		List<StudentDto> students = studentMapper.toDto(studentService.findAll());
        students.sort(Comparator.comparing(StudentDto::userId));
		model.addAttribute("students", students);
		return ("students/students");
	}

	@GetMapping("/{id}")
	public String viewStudent(@PathVariable Long id, Model model, HttpServletResponse response) {
		try {
			Student student = studentService.findById(id);
			model.addAttribute("student", student);
			return ("students/student-detail");
		} catch (ServiceException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			model.addAttribute("errorMessage", e.getMessage());
			return ("error/404");
		}
	}
}
