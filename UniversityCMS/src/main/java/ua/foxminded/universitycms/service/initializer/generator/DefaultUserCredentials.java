package ua.foxminded.universitycms.service.initializer.generator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
enum DefaultUserCredentials {
	ADMIN("admin", "admin", "ADMIN"),
	TEACHER("teacher", "teacher", "TEACHER"),
	STUDENT("student", "student", "STUDENT"),
	REGISTRAR("registrar", "registrar", "REGISTRAR");

	private final String username;
	private final String password;
	private final String roleName;
}
