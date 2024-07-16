package ua.foxminded.universitycms.controller;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;

import ua.foxminded.universitycms.service.initializer.DatabaseInitializerService;

@Controller
public class DatabaseInitializerController implements ApplicationRunner{
	public final DatabaseInitializerService databaseInitializerService;
	
	public DatabaseInitializerController(DatabaseInitializerService databaseInitializerService) {
		this.databaseInitializerService = databaseInitializerService;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		databaseInitializerService.run();
		
	}

}
