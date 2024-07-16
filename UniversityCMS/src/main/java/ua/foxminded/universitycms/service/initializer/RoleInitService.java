package ua.foxminded.universitycms.service.initializer;

import org.springframework.stereotype.Service;

import ua.foxminded.universitycms.entity.Role;
import ua.foxminded.universitycms.service.RoleService;

@Service
public class RoleInitService {
	private final RoleService roleService;

	public RoleInitService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void init() {
		roleService.addRole(new Role("ADMIN", "Admnistrator"));
		roleService.addRole(new Role("GUEST", "Guest"));
		roleService.addRole(new Role("STUDENT", "Student"));
		roleService.addRole(new Role("TEACHER", "Teacher"));
	}
}
