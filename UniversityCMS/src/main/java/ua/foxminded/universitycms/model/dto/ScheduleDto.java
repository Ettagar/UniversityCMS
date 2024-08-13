package ua.foxminded.universitycms.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.foxminded.universitycms.model.Classroom;
import ua.foxminded.universitycms.model.LessonType;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
	private Long scheduleId;
	private CourseDto course;
	private TeacherDto teacher;
	private Classroom classroom;
	private LocalDateTime lessonStart;
	private LocalDateTime lessonEnd;
	private LessonType lessonType;
	private List<StudentDto> students;
}
