package ua.foxminded.universitycms.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import ua.foxminded.universitycms.model.Classroom;
import ua.foxminded.universitycms.model.LessonType;

public record ScheduleDto(
	Long scheduleId,
	CourseDto course,
	TeacherDto teacher,
	Classroom classroom,
	LocalDateTime lessonStart,
	LocalDateTime lessonEnd,
	LessonType lessonType,
	List<StudentDto> students
) {

	public static ScheduleDto createEmpty() {
		return new ScheduleDto(
			null,  // scheduleId
			null,  // course
			null,  // teacher
			null,  // classroom
			null,  // lessonStart
			null,  // lessonEnd
			null,  // lessonType
			List.of()  // students
		);
	}
}
