package ua.foxminded.universitycms.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Role;
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.repository.RoleRepository;
import ua.foxminded.universitycms.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void addUser(User user) {
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional(readOnly = true)
	public User findById(Long id) throws ServiceException {
		return userRepository.findById(id).orElseThrow(() -> new ServiceException("User not found"));
	}

	@Transactional(readOnly = true)
	public User findByUsername(String name) throws ServiceException {
		return userRepository.findByUsername(name).orElseThrow(() -> new ServiceException("User not found"));
	}

	@Transactional
	public void updateUser(Long id, User user, List<Long> roleIds) throws ServiceException {
	    User userToUpdate = userRepository.findById(id).orElseThrow(() -> new ServiceException("User not found"));

	    userToUpdate.setUsername(user.getUsername());
	    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
	        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
	    }
	    if (userToUpdate.getPerson() != null && user.getPerson() != null) {
	        userToUpdate.getPerson().setFirstName(user.getPerson().getFirstName());
	        userToUpdate.getPerson().setLastName(user.getPerson().getLastName());
	        userToUpdate.getPerson().setDateOfBirth(user.getPerson().getDateOfBirth());
	        userToUpdate.getPerson().setEmail(user.getPerson().getEmail());
	        userToUpdate.getPerson().setPhoneNumber(user.getPerson().getPhoneNumber());
	    }

	    userToUpdate.getRoles().clear();
	    for (Long roleId : roleIds) {
	        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ServiceException("Role not found"));
	        userToUpdate.getRoles().add(role);
	    }

	    userRepository.save(userToUpdate);
	}
}
