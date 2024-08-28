package ua.foxminded.universitycms.mapper;

import java.util.List;
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

    public ClassroomDto toDto(Classroom classroom, List<ScheduleDto> schedules) {
        return schedules.stream()
            .filter(schedule -> schedule.classroom().getNumber().equals(classroom.getClassroomId()))
            .findFirst()
            .map(schedule -> new ClassroomDto(
                classroom.getClassroomId(),
                classroom.getNumber(),
                classroom.getFloor(),
                true, // mark as occupied
                schedule.course().courseName(),
                schedule.teacher(),
                schedule.lessonStart(),
                schedule.lessonEnd()
            ))
            .orElseGet(() -> new ClassroomDto(
                classroom.getClassroomId(),
                classroom.getNumber(),
                classroom.getFloor(),
                false, // default to not occupied
                null,  // no course name
                null,  // no teacher
                null,  // no start time
                null   // no end time
            ));
    }

    public List<ClassroomDto> toDto(List<Classroom> classrooms, List<ScheduleDto> schedules) {
        return classrooms.stream()
            .map(classroom -> toDto(classroom, schedules))
            .collect(Collectors.toList());
    }

    public Classroom toModel(ClassroomDto classroomDto) throws ServiceException {
        return classroomService.findById(classroomDto.classroomId());
    }
}