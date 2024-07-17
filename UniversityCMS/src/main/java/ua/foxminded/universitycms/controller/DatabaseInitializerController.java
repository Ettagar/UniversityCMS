package ua.foxminded.universitycms.controller;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.service.initializer.DatabaseInitializerService;

@Controller
@RequiredArgsConstructor
public class DatabaseInitializerController implements ApplicationRunner{
	public final DatabaseInitializerService databaseInitializerService;

	@Override
	public void run(ApplicationArguments args) {
		databaseInitializerService.run();
	}
}
