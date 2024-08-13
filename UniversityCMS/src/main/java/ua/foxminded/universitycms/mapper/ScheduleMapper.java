package ua.foxminded.universitycms.mapper;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.dto.ScheduleDto;
import ua.foxminded.universitycms.service.ScheduleService;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {
	private final StudentMapper studentMapper;
	private final TeacherMapper teacherMapper;
	private final CourseMapper courseMapper;
	private final ScheduleService scheduleService;
	
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
	
	public Schedule toModel(ScheduleDto scheduleDto) throws ServiceException {
		return scheduleService.findById(scheduleDto.getScheduleId());
	}
}
