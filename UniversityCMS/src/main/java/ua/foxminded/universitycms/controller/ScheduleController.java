package ua.foxminded.universitycms.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.CourseMapper;
import ua.foxminded.universitycms.mapper.ScheduleMapper;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.Classroom;
import ua.foxminded.universitycms.model.LessonType;
import ua.foxminded.universitycms.model.Schedule;
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
        List<CourseDto> courses = courseService.findAll()
            .stream()
            .map(courseMapper::toDto)
            .toList();
        List<Classroom> classrooms = classroomService.findAll();
        List<LessonType> lessonTypes = lessonTypeService.findAll();

        model.addAttribute("schedule", new ScheduleDto());
        model.addAttribute("courses", courses);
        model.addAttribute("teachers", List.of());
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("lessonTypes", lessonTypes);

        return "schedules/schedule-create";
    }

    @PostMapping("/schedule-create")
    public String createSchedule(@ModelAttribute ScheduleDto scheduleDto, Model model) {
        try {
            Schedule createdSchedule = scheduleService.createSchedule(
               scheduleMapper.toModel(scheduleDto)
            );

            model.addAttribute("successMessage", "Schedule created successfully");
            model.addAttribute("schedule", createdSchedule);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            return "schedules/schedule-create";
        }

        return "redirect:/schedules";
    }

    @GetMapping
    public String listSchedules(@RequestParam(required = false) Long teacherId,
                                @RequestParam(required = false) Long studentId,
                                @RequestParam(required = false) Long classroomId,
                                @RequestParam(required = false) String startDate,
                                @RequestParam(required = false) String endDate,
                                Model model) {

        LocalDateTime start = (startDate != null && !startDate.isEmpty()) ? LocalDateTime.parse(startDate, formatter) : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = (endDate != null && !endDate.isEmpty()) ? LocalDateTime.parse(endDate, formatter) : LocalDateTime.now().plusMonths(1);

        Set<ScheduleDto> schedules = scheduleService.findSchedules(teacherId, studentId, classroomId, start, end)
            .stream()
            .map(scheduleMapper::toDto)
            .collect(Collectors.toSet());
        List<TeacherDto> teachers = teacherService.findAll()
            .stream()
            .map(teacherMapper::toDto)
            .toList();
        List<StudentDto> students = studentService.findAll()
            .stream()
            .map(studentMapper::toDto)
            .toList();
        List<Classroom> classrooms = classroomService.findAll();

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

    @GetMapping("schedules/course/{courseId}/teachers")
    @ResponseBody
    public ResponseEntity<Set<TeacherDto>> getTeachersByCourse(@PathVariable Long courseId) {
        Set<TeacherDto> teacherDtos;
        try {
            teacherDtos = courseService.getAssignedTeachers(courseId)
                .stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toSet());
        } catch (ServiceException e) {
            log.error("Error fetching teachers for courseId: {}", courseId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.ok(teacherDtos);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception ex) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "error";
    }
}
