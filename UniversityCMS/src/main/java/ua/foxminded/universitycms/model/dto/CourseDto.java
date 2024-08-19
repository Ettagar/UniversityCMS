package ua.foxminded.universitycms.model.dto;

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
			List.of(), // teachers
			List.of()	// students
		);
	}
}
