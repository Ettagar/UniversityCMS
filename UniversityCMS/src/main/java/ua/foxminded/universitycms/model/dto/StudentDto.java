package ua.foxminded.universitycms.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class StudentDto {
	private Long userId;
	private String firstName;
	private String lastName;
	private String groupName;
	private List<String> courses;
}
