package ua.foxminded.universitycms.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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

	@Transactional
    public User addUser(String username, String password, String roleName) throws ServiceException {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new ServiceException("Role not found");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.addRole(role);
        user.setEnabled(true);

        return userRepository.save(user);
    }

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findById(Long id) throws ServiceException {
		return userRepository.findById(id).orElseThrow(() -> new ServiceException("User not found"));
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
