package ua.foxminded.universitycms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.service.StudentService;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.findAll();
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
