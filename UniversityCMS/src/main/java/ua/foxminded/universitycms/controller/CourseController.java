package ua.foxminded.universitycms.controller;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.CourseMapper;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.model.dto.CourseDto;
import ua.foxminded.universitycms.model.dto.StudentDto;
import ua.foxminded.universitycms.model.dto.TeacherDto;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.service.RoleService;
import ua.foxminded.universitycms.service.StudentService;
import ua.foxminded.universitycms.service.TeacherService;
import ua.foxminded.universitycms.service.UserService;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final UserService userService;
    private final RoleService roleService;
    private final TeacherMapper teacherMapper;
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;
    private final UserTools userTools;

    @GetMapping
    public String listCourses(Model model) {
        List<CourseDto> courses = courseMapper.toDto(courseService.findAll());
        courses.sort(Comparator.comparing(CourseDto::courseId));
        model.addAttribute("courses", courses);
        return "courses/courses";
    }

    @GetMapping("/{id}")
    public String viewCourse(@PathVariable Long id, Model model, HttpServletResponse response) throws ServiceException {
        try {
            CourseDto course = courseMapper.toDto(courseService.findById(id));
            model.addAttribute("course", course);
            return "courses/course-detail";
        } catch (ServiceException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("errorMessage", e.getMessage());
            return "error/404";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) throws ServiceException {
        CourseDto course = courseMapper.toDto(courseService.findById(id));
        List<TeacherDto> allTeachers = teacherService.findAll().stream()
            .map(teacherMapper::toDto)
            .toList();
        List<StudentDto> allStudents = studentService.findAll().stream()
            .map(studentMapper::toDto)
            .toList();
        List<TeacherDto> unassignedTeachers = allTeachers.stream()
            .filter(teacher -> !course.teachers().contains(teacher))
            .sorted(Comparator.comparing(TeacherDto::userId))
            .toList();
        List<StudentDto> unenrolledStudents = allStudents.stream()
            .filter(student -> !course.students().contains(student))
            .sorted(Comparator.comparing(StudentDto::userId))
            .toList();
        model.addAttribute("course", course);
        model.addAttribute("unassignedTeachers", unassignedTeachers);
        model.addAttribute("unenrolledStudents", unenrolledStudents);
        return "courses/course-edit";
    }

    @PostMapping("/enroll/{courseId}")
    public String enrollInCourse(@PathVariable Long courseId, Principal principal) throws ServiceException {
        User loggedInUser = userService.findByUsername(principal.getName());

        if (loggedInUser.getRoles().contains(roleService.getRoleByName("STUDENT"))) {
            courseService.addStudentToCourse(courseId, loggedInUser.getUserId());
        }

        return "redirect:/courses/courses-user";
    }

    @PostMapping("/unenroll/{courseId}")
    public String unenrollFromCourse(@PathVariable Long courseId, Principal principal) throws ServiceException {
        User loggedInUser = userService.findByUsername(principal.getName());

        if (loggedInUser.getRoles().contains(roleService.getRoleByName("STUDENT"))) {
            courseService.removeStudentFromCourse(courseId, loggedInUser.getUserId());
        }

        return "redirect:/courses/courses-user";
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable("id") Long id, @ModelAttribute("course") CourseDto courseDto,
                               @RequestParam(required = false) List<Long> studentIds, @RequestParam(required = false) List<Long> teacherIds) throws ServiceException {
    	Course course = courseMapper.toModel(courseDto);
        courseService.updateCourse(course, studentIds, teacherIds);
        return "redirect:/courses";
    }

    @PostMapping("/removeTeacher")
    public String removeTeacher(@RequestParam Long courseId, @RequestParam Long teacherId) throws ServiceException {
        courseService.removeTeacherFromCourse(courseId, teacherId);
        return "redirect:/courses/edit/" + courseId;
    }

    @PostMapping("/removeStudent")
    public String removeStudent(@RequestParam Long courseId, @RequestParam Long studentId) throws ServiceException {
        courseService.removeStudentFromCourse(courseId, studentId);
        return "redirect:/courses/edit/" + courseId;
    }

    @PostMapping("/addTeacher")
    public String addTeacher(@RequestParam Long courseId, @RequestParam Long teacherId) throws ServiceException {
        courseService.addTeacherToCourse(courseId, teacherId);
        return "redirect:/courses/edit/" + courseId;
    }

    @PostMapping("/addStudent")
    public String addStudent(@RequestParam Long courseId, @RequestParam Long studentId) throws ServiceException {
        courseService.addStudentToCourse(courseId, studentId);
        return "redirect:/courses/edit/" + courseId;
    }

    @GetMapping("/course-create")
    public String showCreateForm(Model model) {
       model.addAttribute("course", CourseDto.createEmpty());
        return "courses/course-create";
    }

    @PostMapping("/course-create")
    public String saveCourse(@ModelAttribute("course") CourseDto courseDto) throws ServiceException {
        courseService.addCourse(courseMapper.toModel(courseDto));
        return "redirect:/courses";
    }

    @GetMapping("/courses-user")
    public String viewMyCourses(Model model) throws ServiceException {
        User loggedInUser = userTools.getLoggedInUser();
        List<CourseDto> courses = userTools.assignedCourses(loggedInUser);
        List<CourseDto> unenrolledCourses = userTools.getNotAssignedCourses(loggedInUser);
        model.addAttribute("courses", courses);
        model.addAttribute("unenrolledCourses", unenrolledCourses);
        return "courses/courses-user";
    }

    @PostMapping("/enroll-course")
    public String enrollCourse(@RequestParam Long courseId) throws ServiceException {
        User loggedInUser = userTools.getLoggedInUser();
        courseService.addStudentToCourse(courseId, loggedInUser.getUserId());
        return "redirect:/courses/courses-user";
    }

    @GetMapping("/get-courses-list")
    @ResponseBody
    public ResponseEntity<List<CourseDto>> getCoursesNames() {
        List<CourseDto> simpleCourses = courseMapper.toDto(courseService.findAll());
        return ResponseEntity.ok(simpleCourses);
    }
}
