package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.universitycms.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	Role findByName(String roleName);
}
