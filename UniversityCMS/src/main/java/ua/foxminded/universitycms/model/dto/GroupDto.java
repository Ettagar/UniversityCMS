package ua.foxminded.universitycms.model.dto;

import java.util.Collections;
import java.util.List;

public record GroupDto (
	Long groupId,
	String groupName,
	List<StudentDto> students
) {

	public static GroupDto createEmpty() {
		return new GroupDto(
			null, // groupId
			null, // groupName
			Collections.emptyList()	// students
		);
	}
}
