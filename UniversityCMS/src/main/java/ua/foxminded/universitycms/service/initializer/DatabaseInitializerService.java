package ua.foxminded.universitycms.service.initializer;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import ua.foxminded.universitycms.service.initializer.generator.ClassroomGeneratorService;
import ua.foxminded.universitycms.service.initializer.generator.CoursesGeneratorService;
import ua.foxminded.universitycms.service.initializer.generator.GroupsGeneratorService;
import ua.foxminded.universitycms.service.initializer.generator.PersonsGeneratorService;
import ua.foxminded.universitycms.service.initializer.generator.StudentsGeneratorService;
import ua.foxminded.universitycms.service.initializer.generator.TeachersGeneratorService;

@Service
@Profile("!test")
public class DatabaseInitializerService {
	private static final Logger log = LogManager.getLogger(DatabaseInitializerService.class.getName());
	private final CoursesGeneratorService coursesGeneratorService;
	private final GroupsGeneratorService groupsGeneratorService;
	private final PersonsGeneratorService personsGeneratorService;
	private final StudentsGeneratorService studentsGeneratorService;
	private final TeachersGeneratorService teachersGeneratorService;
	private final RoleInitService roleInitService;
	private final ClassroomGeneratorService classroomGeneratorService;
	private final LessonTypeInitService lessonTypeInitService;
	
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_GREEN = "\u001B[32m";

	public DatabaseInitializerService(CoursesGeneratorService coursesGeneratorService,
			GroupsGeneratorService groupsGeneratorService, PersonsGeneratorService personsGeneratorService,
			StudentsGeneratorService studentsGeneratorService, TeachersGeneratorService teachersGeneratorService,
			RoleInitService roleInitService, ClassroomGeneratorService classroomGeneratorService,
			LessonTypeInitService lessonTypeInitService) {
		this.coursesGeneratorService = coursesGeneratorService;
		this.groupsGeneratorService = groupsGeneratorService;
		this.personsGeneratorService = personsGeneratorService;
		this.studentsGeneratorService = studentsGeneratorService;
		this.teachersGeneratorService = teachersGeneratorService;
		this.roleInitService = roleInitService;
		this.classroomGeneratorService = classroomGeneratorService;
		this.lessonTypeInitService = lessonTypeInitService;
		
	}

	public void run() {
		try {	
			    roleInitService.init();
				personsGeneratorService.generate();
				groupsGeneratorService.generate();
				coursesGeneratorService.generate();
				teachersGeneratorService.generate();				
				studentsGeneratorService.generate();
				classroomGeneratorService.generate();
				lessonTypeInitService.init();
				
		System.out.println(ANSI_GREEN + "Database check and generation completed" + ANSI_RESET);
		} catch (Exception e) {
            log.error("Database initialization failed", e);
        }
	}
}
