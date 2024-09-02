package ua.foxminded.universitycms.controller;

import java.time.LocalDateTime;
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
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.model.dto.ClassroomDto;
import ua.foxminded.universitycms.model.dto.ScheduleDto;
import ua.foxminded.universitycms.model.dto.TeacherDto;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.service.LessonTypeService;
import ua.foxminded.universitycms.service.RoleService;
import ua.foxminded.universitycms.service.ScheduleService;
import ua.foxminded.universitycms.service.TeacherService;

@Slf4j
@Controller
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final LessonTypeService lessonTypeService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final RoleService roleService;
    private final TeacherMapper teacherMapper;
    private final ScheduleMapper scheduleMapper;
    private final CourseMapper courseMapper;
    private final ScheduleTools scheduleTools;
    private final UserTools userTools;

    @GetMapping("/schedule-create")
    public String showCreateForm(Model model) {

    	model.addAttribute("schedule", ScheduleDto.createEmpty());
    	model.addAttribute("lessonTypes", lessonTypeService.findAll());
        return "schedules/schedule-create";
    }

    @PostMapping("/schedule-create")
    public String createSchedule(@ModelAttribute("schedule") ScheduleDto scheduleDto) throws ServiceException {
    	scheduleService.addSchedule(scheduleMapper.toModel(scheduleDto));
    	return "redirect:/schedules";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) throws ServiceException {
        ScheduleDto schedule = scheduleMapper.toDto(scheduleService.findById(id));

        List<ClassroomDto> classrooms = scheduleTools.getClassroomsDropdownData(
        		schedule.lessonStart(),
        		schedule.lessonEnd(),
        		schedule.scheduleId());

        model.addAttribute("lessonTypes", lessonTypeService.findAll());
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("courses", courseMapper.toDto(courseService.findAll()));
    	model.addAttribute("teachers", teacherMapper.toDto(teacherService.findAll()));
    	model.addAttribute("schedule", schedule);
        return "schedules/schedule-create";
    }

    @PostMapping("/edit/{id}")
    public String updateSchedule(@PathVariable("id") Long id, @ModelAttribute("schedule") ScheduleDto scheduleDto) throws ServiceException {
        scheduleService.addSchedule(scheduleMapper.toModel(scheduleDto));

        return "redirect:/schedules";
    }

    @GetMapping
    public String listSchedules(@RequestParam(required = false) Long courseId,
    							@RequestParam(required = false) Long teacherId,
                                @RequestParam(required = false) Long studentId,
                                @RequestParam(required = false) Long classroomId,
                                @RequestParam(required = false) String startDate,
                                @RequestParam(required = false) String endDate,
                                Model model) {

        LocalDateTime start = (startDate != null && !startDate.isEmpty())
        			? scheduleTools.formatFromDatetimeLocal(startDate)
        			: LocalDateTime.now().minusMonths(1);
        LocalDateTime end = (endDate != null && !endDate.isEmpty())
        			? scheduleTools.formatFromDatetimeLocal(endDate)
        			: LocalDateTime.now().plusMonths(1);

        List<ScheduleDto> schedules = scheduleMapper.toDto(
        		scheduleService.findSchedules(courseId, teacherId, studentId, classroomId, start, end));

        scheduleTools.getAllDefaultDropdownData(model);
        model.addAttribute("schedules", schedules);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("selectedCourseId", courseId);
        model.addAttribute("selectedTeacherId", teacherId);
        model.addAttribute("selectedStudentId", studentId);
        model.addAttribute("selectedClassroomId", classroomId);

        return "schedules/schedules";
    }

    @GetMapping("/my-schedule")
    public String listMySchedules(Model model) throws ServiceException {
    	User loggedInUser = userTools.getLoggedInUser();
        Role roleStudent = roleService.getRoleByName("STUDENT");
        Role roleTeacher = roleService.getRoleByName("TEACHER");

        if (loggedInUser.getRoles().contains(roleStudent)) {
        	return listSchedules(null, null, loggedInUser.getUserId(), null, null, null, model);
        } else if (loggedInUser.getRoles().contains(roleTeacher)) {
        	return listSchedules(null, loggedInUser.getUserId(), null, null, null, null, model);
        }
        return "redirect:/schedules";
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

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable("id") Long id) {
        try {
            scheduleService.deleteSchedule(id);
            return "redirect:/schedules";
        } catch (ServiceException e) {
            log.error("Failed to delete schedule with id {}: {}", id, e.getMessage());
            return "error/404";
        }
    }

    @GetMapping("/course/{courseId}/teachers")
    @ResponseBody
    public ResponseEntity<List<TeacherDto>> getTeachersByCourse(
            @PathVariable Long courseId,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) throws ServiceException {

        return ResponseEntity.ok(scheduleTools.getTeachersDropdownData(startDate, endDate, courseId));
    }

    @GetMapping("/getClassrooms")
    @ResponseBody
    public ResponseEntity<List<ClassroomDto>> getClassrooms(
    		@RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("scheduleId") Long scheduleId) {

        return ResponseEntity.ok(scheduleTools.getClassroomsDropdownData(startDate, endDate, scheduleId));
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception ex) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());

        return "error";
    }
}
