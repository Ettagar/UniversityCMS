package ua.foxminded.universitycms.service;

import org.springframework.stereotype.Service;

import ua.foxminded.universitycms.entity.Role;
import ua.foxminded.universitycms.repository.RoleRepository;

@Service
public class RoleService{
	private final RoleRepository roleRepository;
	
	public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
	
	public void addRole(Role role) {
		roleRepository.save(role);
	}
	
	public Role getRoleById(int id) throws ServiceException {
		try {
		return roleRepository.findById(id).orElseThrow(() -> new ServiceException("Role not found"));
		} catch (IllegalArgumentException e) {
			throw new ServiceException("Role_ID cannot be NULL");
		}
    }
}
