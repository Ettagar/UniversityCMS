package ua.foxminded.universitycms.model.enums;

import java.util.List;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ua.foxminded.universitycms.model.Role;

@Getter
@AllArgsConstructor
public enum RoleEnum {
	ADMIN("ADMIN", "Administrator"),
	GUEST("GUEST", "Guest"),
	USER("USER", "User"),
	STUDENT("STUDENT", "Student"),
	TEACHER("TEACHER", "Teacher"),
	REGISTRAR("REGISTRAR","Registrar");

	private final String name;
	private final String description;

	public Role toRole() {
		return new Role(this.getName(), this.getDescription());
	}

	public static List<Role> toRoleList() {
		return Stream.of(RoleEnum.values()).map(RoleEnum::toRole).toList();
	}
}
