package ua.foxminded.universitycms.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.CourseMapper;
import ua.foxminded.universitycms.mapper.ScheduleMapper;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.dto.ClassroomDto;
import ua.foxminded.universitycms.model.dto.ScheduleDto;
import ua.foxminded.universitycms.model.dto.TeacherDto;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.service.LessonTypeService;
import ua.foxminded.universitycms.service.ScheduleService;
import ua.foxminded.universitycms.service.StudentService;
import ua.foxminded.universitycms.service.TeacherService;

@Component
@RequiredArgsConstructor
public class ScheduleTools {
    private final ScheduleService scheduleService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final LessonTypeService lessonTypeService;
    private final TeacherMapper teacherMapper;
    private final ScheduleMapper scheduleMapper;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public List<ClassroomDto> getClassroomsDropdownData (LocalDateTime startDate,
    			LocalDateTime endDate,	Long scheduleId) {
    	List<ScheduleDto> schedules = scheduleMapper.toDto(scheduleService.findSchedules(
    			null,	//Any course
    			null,	//Any teacher
    			null,	//Any student
    			null,	//Any classroom
    			startDate, endDate)); //Between this dates

    	if (scheduleId != 0) {
    	    schedules.removeIf(schedule -> schedule.scheduleId().equals(scheduleId));
    	}

    	return scheduleService.getAllClassroomsWithOccupancy(schedules);
    }

    public List<TeacherDto> getTeachersDropdownData (LocalDateTime startDate,
    			LocalDateTime endDate,	Long courseId) throws ServiceException {
    	List<TeacherDto> teachers = teacherMapper.toDto(courseService.getAssignedTeachers(courseId));

    	return teachers.stream()
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
    }

    public void getAllDefaultDropdownData(Model model) {
    	model.addAttribute("courses", courseMapper.toDto(courseService.findAll()));
    	model.addAttribute("teachers", teacherMapper.toDto(teacherService.findAll()));
    	model.addAttribute("students", studentMapper.toDto(studentService.findAll()));
    	model.addAttribute("lessonTypes", lessonTypeService.findAll());
    }

    public String formatToDatetimeLocal (LocalDateTime date) {
    	return date != null ? date.format(formatter) : null;
    }

    public LocalDateTime formatFromDatetimeLocal (String date) {
    	return date != null ? LocalDateTime.parse(date, formatter) : null;
    }
}
