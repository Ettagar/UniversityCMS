package ua.foxminded.universitycms.service.initializer.generator;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUsersGeneratorService {
    private final UserService userService;

	@Transactional
	public void generate() {
		log.info("Generating default users...");
		System.out.println("Generating default users...");
		try {
			userService.addUser("admin", "admin", "ADMIN");
			userService.addUser("teacher", "teacher", "TEACHER");
			userService.addUser("student", "student", "STUDENT");
		} catch (Exception e) {
			log.error("Error generating default users", e);
		}
		System.out.println("Default users were generated and added to DB");
	}
}
