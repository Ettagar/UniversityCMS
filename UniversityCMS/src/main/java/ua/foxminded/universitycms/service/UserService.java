package ua.foxminded.universitycms.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.entity.User;
import ua.foxminded.universitycms.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public void addUser(User user) {
		userRepository.save(user);
	}
}
