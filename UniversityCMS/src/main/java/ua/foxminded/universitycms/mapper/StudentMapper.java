package ua.foxminded.universitycms.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.dto.StudentDto;
import ua.foxminded.universitycms.service.StudentService;

@Component
@RequiredArgsConstructor
public class StudentMapper {

	public StudentDto toDto(Student student) {
		return new StudentDto(
				student.getUserId(),
				student.getPerson().getFirstName(),
				student.getPerson().getLastName(),
                student.getGroup().getGroupName(),
                student.getPerson().getEmail(),
				student.getCourses().stream()
                    .map(Course::getCourseName)
                    .toList());
	}

	public List<StudentDto> toDto(List<Student> students) {
		return students.stream()
                .map(this :: toDto)
                .collect(Collectors.toList());
	}

	public Student toModel(StudentDto studentDto, StudentService studentService) throws ServiceException {
		return studentService.findById(studentDto.userId());
	}
}
