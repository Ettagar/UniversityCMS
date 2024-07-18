package ua.foxminded.universitycms.service.initializer;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.model.enums.RoleEnum;
import ua.foxminded.universitycms.service.RoleService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleInitService {
	private final RoleService roleService;

	public void init() {
        List<Role> roles = RoleEnum.toRoleList();
        roles.forEach(roleService::addRole);
        log.info("Roles were generated");
        System.out.println("Roles were created");
    }
}
