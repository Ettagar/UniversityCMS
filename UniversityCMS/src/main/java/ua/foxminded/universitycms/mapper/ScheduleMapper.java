package ua.foxminded.universitycms.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.dto.ScheduleDto;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.service.TeacherService;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {
	private final StudentMapper studentMapper;
	private final TeacherMapper teacherMapper;
	private final CourseMapper courseMapper;
	private final CourseService courseService;
	private final TeacherService teacherService;

	@Transactional(readOnly = true)
	public ScheduleDto toDto(Schedule schedule) {
		return new ScheduleDto(
				schedule.getScheduleId(),
				courseMapper.toDto(schedule.getCourse()),
				teacherMapper.toDto(schedule.getTeacher()),
				schedule.getClassroom(),
				schedule.getLessonStart(),
				schedule.getLessonEnd(),
				schedule.getLessonType(),
				schedule.getStudents().stream()
					.map(studentMapper::toDto)
					.toList());
	}

	@Transactional(readOnly = true)
    public Schedule toModel(ScheduleDto scheduleDto) throws ServiceException {
		Course course = courseService.findById(scheduleDto.course().courseId());
		
		Schedule schedule = new Schedule();
        schedule.setScheduleId(scheduleDto.scheduleId());       
        schedule.setCourse(course);
        schedule.setTeacher(teacherService.findById(scheduleDto.teacher().userId()));
        schedule.setClassroom(scheduleDto.classroom());
        schedule.setLessonStart(scheduleDto.lessonStart());
        schedule.setLessonEnd(scheduleDto.lessonEnd());
        schedule.setLessonType(scheduleDto.lessonType());
        schedule.setStudents(new ArrayList<>(course.getStudents()));
        
        return schedule;
    }

	public List<ScheduleDto> toDto(List<Schedule> schedules) {
		return schedules.stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}
}
