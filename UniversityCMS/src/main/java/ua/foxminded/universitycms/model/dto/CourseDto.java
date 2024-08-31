package ua.foxminded.universitycms.model.dto;

import java.util.Collections;
import java.util.List;

public record CourseDto(
	Long courseId,
	String courseName,
	String courseDescription,
	List<TeacherDto> teachers,
	List<StudentDto> students
) {

	public static CourseDto createEmpty() {
		return new CourseDto(
			null, // courseId
			null, // courseName
			null, // courseDescription
			Collections.emptyList(), // teachers
			Collections.emptyList()	// students
		);
	}
}
