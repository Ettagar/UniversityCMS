package ua.foxminded.universitycms.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.CourseMapper;
import ua.foxminded.universitycms.mapper.ScheduleMapper;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.Classroom;
import ua.foxminded.universitycms.model.LessonType;
import ua.foxminded.universitycms.model.dto.ClassroomDto;
import ua.foxminded.universitycms.model.dto.CourseDto;
import ua.foxminded.universitycms.model.dto.ScheduleDto;
import ua.foxminded.universitycms.model.dto.StudentDto;
import ua.foxminded.universitycms.model.dto.TeacherDto;
import ua.foxminded.universitycms.service.ClassroomService;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.service.LessonTypeService;
import ua.foxminded.universitycms.service.ScheduleService;
import ua.foxminded.universitycms.service.StudentService;
import ua.foxminded.universitycms.service.TeacherService;

@Slf4j
@Controller
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ClassroomService classroomService;
    private final LessonTypeService lessonTypeService;
    private final TeacherMapper teacherMapper;
    private final StudentMapper studentMapper;
    private final ScheduleMapper scheduleMapper;
    private final CourseMapper courseMapper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @GetMapping("/schedule-create")
    public String showCreateForm(Model model) {
        List<CourseDto> courses = courseMapper.toDto(courseService.findAll());
        List<LessonType> lessonTypes = lessonTypeService.findAll();

        model.addAttribute("schedule", ScheduleDto.createEmpty());
        model.addAttribute("courses", courses);
        model.addAttribute("teachers", List.of());
        model.addAttribute("classrooms", List.of());
        model.addAttribute("lessonTypes", lessonTypes);

        return "schedules/schedule-create";
    }

    @PostMapping("/schedule-create")
    public String createSchedule(@ModelAttribute("schedule") ScheduleDto scheduleDto) throws ServiceException {
    	scheduleService.addSchedule(scheduleMapper.toModel(scheduleDto));
    	return "redirect:/schedules";
    }

    @GetMapping
    public String listSchedules(@RequestParam(required = false) Long teacherId,
                                @RequestParam(required = false) Long studentId,
                                @RequestParam(required = false) Long classroomId,
                                @RequestParam(required = false) String startDate,
                                @RequestParam(required = false) String endDate,
                                Model model) {

        LocalDateTime start = (startDate != null && !startDate.isEmpty())
        			? LocalDateTime.parse(startDate, formatter) : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = (endDate != null && !endDate.isEmpty())
        			? LocalDateTime.parse(endDate, formatter) : LocalDateTime.now().plusMonths(1);

        List<ScheduleDto> schedules = scheduleMapper.toDto(
        		scheduleService.findSchedules(teacherId, studentId, classroomId, start, end));
        List<CourseDto> courses = courseMapper.toDto(courseService.findAll());
        List<TeacherDto> teachers = teacherMapper.toDto(teacherService.findAll());
        List<StudentDto> students = studentMapper.toDto(studentService.findAll());
        List<Classroom> classrooms = classroomService.findAll();

        model.addAttribute("courses", courses);
        model.addAttribute("schedules", schedules);
        model.addAttribute("teachers", teachers);
        model.addAttribute("students", students);
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("startDate", start.format(formatter));
        model.addAttribute("endDate", end.format(formatter));
        model.addAttribute("selectedTeacherId", teacherId);
        model.addAttribute("selectedStudentId", studentId);
        model.addAttribute("selectedClassroomId", classroomId);

        return "schedules/schedules";
    }

    @GetMapping("/{id}")
    public String viewSchedule(@PathVariable Long id, Model model, HttpServletResponse response) throws ServiceException {
        try {
        	ScheduleDto schedule = scheduleMapper.toDto(scheduleService.findById(id));
            model.addAttribute("schedule", schedule);

            return "schedules/schedule-detail";
        } catch (ServiceException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("errorMessage", e.getMessage());

            return "error/404";
        }
    }

    @GetMapping("/course/{courseId}/teachers")
    @ResponseBody
    public ResponseEntity<List<TeacherDto>> getTeachersByCourse(
            @PathVariable Long courseId,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) throws ServiceException {

        List<TeacherDto> teachers = teacherMapper.toDto(courseService.getAssignedTeachers(courseId));
        List<TeacherDto> checkedTeachers = teachers.stream()
                .map(teacher -> {
                    boolean isAvailable = scheduleService.findSchedulesByTeacher(teacher.userId(), startDate, endDate)
                    		.isEmpty();
                    return new TeacherDto(
                            teacher.userId(),
                            teacher.firstName(),
                            teacher.lastName(),
                            teacher.courses(),
                            isAvailable
                    );
                })
                .toList();

        return ResponseEntity.ok(checkedTeachers);
    }

    @GetMapping("/getClassrooms")
    @ResponseBody
    public ResponseEntity<List<ClassroomDto>> getClassrooms(
    		@RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {

        List<ScheduleDto> schedules = scheduleMapper.toDto(scheduleService.findSchedules(null, null, null, startDate, endDate));
        List<ClassroomDto> classrooms = scheduleService.getAllClassroomsWithOccupancy(schedules);

        return ResponseEntity.ok(classrooms);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception ex) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());

        return "error";
    }
}
