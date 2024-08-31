package ua.foxminded.universitycms.model.dto;

import java.time.LocalDateTime;
import java.util.Collections;
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
			CourseDto.createEmpty(),  // course
			TeacherDto.createEmpty(),  // teacher
			new Classroom(),  // classroom
			null,  // lessonStart
			null,  // lessonEnd
			new LessonType(),  // lessonType
			Collections.emptyList()  // students
		);
	}
}
