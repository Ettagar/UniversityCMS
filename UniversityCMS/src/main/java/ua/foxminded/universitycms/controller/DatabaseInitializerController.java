package ua.foxminded.universitycms.controller;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.service.initializer.DatabaseInitializerService;

@Controller
@Profile("!test")
@RequiredArgsConstructor
public class DatabaseInitializerController implements ApplicationRunner {
	public final DatabaseInitializerService databaseInitializerService;

	@Override
	public void run(ApplicationArguments args) {
		databaseInitializerService.run();
	}
}
