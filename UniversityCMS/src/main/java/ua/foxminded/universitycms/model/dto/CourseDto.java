package ua.foxminded.universitycms.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class CourseDto {
	private Long courseId;
	
	@NonNull
	private String courseName;
	
	@NonNull
	private String courseDescription;
	
	private List<TeacherDto> teachers;
	
	private List<StudentDto> students;
}
