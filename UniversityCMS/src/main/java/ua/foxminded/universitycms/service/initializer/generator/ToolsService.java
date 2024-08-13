package ua.foxminded.universitycms.service.initializer.generator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToolsService {
	private final PasswordEncoder passwordEncoder;
	private static final int PASSWORD_LENGTH = 8;
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";
	private static final SecureRandom random = new SecureRandom();

	public String generateRandomPassword() {
		StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}

		return passwordEncoder.encode(password);
	}

	public <T> List<T> getRandomNElements(List<T> list, int n) {
		if (n > list.size()) {
			throw new IllegalArgumentException("N cannot be larger than the size of the list");
		}

		List<T> copy = new ArrayList<>(list);
		Collections.shuffle(copy);
		return copy.subList(0, n);
	}
}
