package ua.foxminded.universitycms.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Classroom;
import ua.foxminded.universitycms.model.dto.ClassroomDto;
import ua.foxminded.universitycms.model.dto.ScheduleDto;
import ua.foxminded.universitycms.service.ClassroomService;

@Component
@RequiredArgsConstructor
public class ClassroomMapper {

	private final ClassroomService classroomService;

    public ClassroomDto toDto(Classroom classroom, Map<Long, ScheduleDto> scheduleMap) {
    	ScheduleDto schedule = scheduleMap.get(classroom.getClassroomId());

    	if (schedule != null) {
            // Classroom is occupied
            return new ClassroomDto(
                classroom.getClassroomId(),
                classroom.getNumber(),
                classroom.getFloor(),
                schedule.course().courseName(),
                schedule.teacher(),
                schedule.lessonStart(),
                schedule.lessonEnd(),
                true // mark as occupied
            );
        } else {
            // Classroom is not occupied
            return new ClassroomDto(
                classroom.getClassroomId(),
                classroom.getNumber(),
                classroom.getFloor(),
                null,  // no course name
                null,  // no teacher
                null,  // no start time
                null,  // no end time
                false // default to not occupied
            );
        }
    }

    public List<ClassroomDto> toDto(List<Classroom> classrooms, Map<Long, ScheduleDto> scheduleMap) {
        return classrooms.stream()
            .map(classroom -> toDto(classroom, scheduleMap))
            .collect(Collectors.toList());
    }

    public Classroom toModel(ClassroomDto classroomDto) throws ServiceException {
        return classroomService.findById(classroomDto.classroomId());
    }
}
