package ua.foxminded.universitycms.service.initializer.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.entity.Classroom;
import ua.foxminded.universitycms.repository.ClassroomRepository;

@Service
@RequiredArgsConstructor
public class ClassroomGeneratorService {
	private static final Logger log = LoggerFactory.getLogger(ClassroomGeneratorService.class.getName());
	private final ClassroomRepository classroomRepository;
	private static final int FLOORS_COUNT = 3;
	private static final int CLASSROOMS_PER_FLOOR = 10;

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
