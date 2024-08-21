package ua.foxminded.universitycms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
	private final RoleRepository roleRepository;

	@Transactional
	public void addRole(Role role) {
		roleRepository.save(role);
	}

	@Transactional(readOnly = true)
	public Role getRoleById(Long id) throws ServiceException {
		try {
			return roleRepository.findById(id).orElseThrow(() -> new ServiceException("Role not found"));
		} catch (IllegalArgumentException e) {
			throw new ServiceException("Role_ID cannot be NULL");
		}
	}

	@Transactional(readOnly = true)
	public Role getRoleByName(String roleName) {
		return roleRepository.findByName(roleName);
	}

	@Transactional(readOnly = true)
	public List<Role> findAll() {
		return roleRepository.findAll();
	}
}
