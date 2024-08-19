package ua.foxminded.universitycms.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.dto.TeacherDto;
import ua.foxminded.universitycms.service.TeacherService;

@Component
@RequiredArgsConstructor
public class TeacherMapper {
	private final TeacherService teacherService;

    public TeacherDto toDto(Teacher teacher) {
        return new TeacherDto(
            teacher.getUserId(),
            teacher.getPerson().getFirstName(),
            teacher.getPerson().getLastName(),
			teacher.getCourses().stream()
				.map(Course::getCourseName)
				.toList()
        );
    }

    public List<TeacherDto> toDto(List<Teacher> teachers) {
        return teachers.stream()
                .map(this :: toDto)
                .collect(Collectors.toList());
    }

    public Teacher toModel(TeacherDto teacherDto) throws ServiceException {
        return teacherService.findById(teacherDto.userId());
    }
}
