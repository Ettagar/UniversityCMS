package ua.foxminded.universitycms.service.initializer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.entity.Role;
import ua.foxminded.universitycms.entity.RoleEnum;
import ua.foxminded.universitycms.service.RoleService;

@Service
@RequiredArgsConstructor
public class RoleInitService {
	private static final Logger log = LoggerFactory.getLogger(RoleInitService.class.getName());
	private final RoleService roleService;

	public void init() {
        List<Role> roles = RoleEnum.toRoleList();
        roles.forEach(roleService::addRole);
        log.info("Roles were generated");
        System.out.println("Roles were created");
    }
}
