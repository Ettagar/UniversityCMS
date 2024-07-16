package ua.foxminded.universitycms.service.initializer.generator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import ua.foxminded.universitycms.entity.Classroom;
import ua.foxminded.universitycms.repository.ClassroomRepository;

@Service
public class ClassroomGeneratorService {
	private static final Logger log = LogManager.getLogger(ClassroomGeneratorService.class.getName());
	private final ClassroomRepository classroomRepository;
	private static final int FLOORS_COUNT = 3;
	private static final int CLASSROOMS_PER_FLOOR = 10;


	public ClassroomGeneratorService(ClassroomRepository classroomRepository) {
		this.classroomRepository = classroomRepository;
	}

	public void generate() {
		for (int i = 1; i <= FLOORS_COUNT; i++) {
			for (int j = 1; j <= CLASSROOMS_PER_FLOOR; j++) {
				Classroom classroom = new Classroom();
				classroom.setFloor(i);
				classroom.setNumber(String.valueOf(i * 100 + j));
				classroomRepository.save(classroom);
				log.info("Classroom {} was generated and saved to DB", classroom.getNumber());
			}
		}
		log.info("Classrooms were generated");
		System.out.println("Classrooms were created");
	}
}
