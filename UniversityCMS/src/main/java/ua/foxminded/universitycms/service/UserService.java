package ua.foxminded.universitycms.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	@Transactional
	public void addUser(User user) {
		userRepository.save(user);
	}
}
