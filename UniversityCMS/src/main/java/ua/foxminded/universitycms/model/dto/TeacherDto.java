package ua.foxminded.universitycms.model.dto;

import java.util.Collections;
import java.util.List;

public record TeacherDto (
    Long userId,
    String firstName,
    String lastName,
    List<String> courses,
    Boolean available
) {
	public static TeacherDto createEmpty() {
		return new TeacherDto(
				null,	//userId
				null,	//firstName
				null, 	//lastName
				Collections.emptyList(),	//courses
				null	//available
		);
	}
}
