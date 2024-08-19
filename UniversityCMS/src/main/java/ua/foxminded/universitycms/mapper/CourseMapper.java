package ua.foxminded.universitycms.mapper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.dto.CourseDto;
import ua.foxminded.universitycms.model.dto.StudentDto;
import ua.foxminded.universitycms.model.dto.TeacherDto;
import ua.foxminded.universitycms.service.CourseService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseMapper {
	private final StudentMapper studentMapper;
	private final TeacherMapper teacherMapper;
	private final CourseService courseService;

	public CourseDto toDto(Course course) {
		List<TeacherDto> teachers = course.getTeachers().stream()
				.map(teacherMapper::toDto)
				.sorted(Comparator.comparing(TeacherDto::userId, Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());

		List<StudentDto> students = course.getStudents().stream()
				.map(studentMapper::toDto)
				.sorted(Comparator.comparing(StudentDto::userId, Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());

		return new CourseDto(course.getCourseId(), course.getCourseName(), course.getCourseDescription(), teachers,
				students);
	}

	public List<CourseDto> toDto(List<Course> courses) {
		return courses.stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	public Course toModel(CourseDto courseDto) throws ServiceException {
	    if (courseDto.courseId() != null) {
	        Optional<Course> existingCourse = Optional.ofNullable(courseService.findById(courseDto.courseId()));
	        if (existingCourse.isPresent()) {
	            Course course = existingCourse.get();
	            course.setCourseName(courseDto.courseName());
	            course.setCourseDescription(courseDto.courseDescription());
	            return course;
	        }
	    }
	    return createNewCourse(courseDto);
	}

	private Course createNewCourse(CourseDto courseDto) {
	    return new Course(
	            courseDto.courseName(),
	            courseDto.courseDescription()
	    );
	}
}
