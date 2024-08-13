package ua.foxminded.universitycms.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class GroupDto {
	private Long groupId;
	
	@NonNull
	private String groupName;
	
	private List<StudentDto> students;
}
