package ua.foxminded.universitycms.model.dto;

import java.util.List;

public record StudentDto (
	Long userId,
	String firstName,
	String lastName,
	String groupName,
	List<String> courses
) {
}
