package ua.foxminded.universitycms.model.dto;

import java.util.List;

public record TeacherDto (
    Long userId,
    String firstName,
    String lastName,
    List<String> courses
) {
	public static TeacherDto createEmpty() {
		return new TeacherDto(
				null,	//userId
				"",		//firstName
				"", 	//lastName
				List.of());		//courses
	}	
}
