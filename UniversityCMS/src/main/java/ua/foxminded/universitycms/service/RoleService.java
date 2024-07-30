package ua.foxminded.universitycms.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
	private final RoleRepository roleRepository;

	public void addRole(Role role) {
		roleRepository.save(role);
	}

	public Role getRoleById(Long id) throws ServiceException {
		try {
			return roleRepository.findById(id).orElseThrow(() -> new ServiceException("Role not found"));
		} catch (IllegalArgumentException e) {
			throw new ServiceException("Role_ID cannot be NULL");
		}
	}
}
