package ua.foxminded.universitycms.service.initializer.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.model.Classroom;
import ua.foxminded.universitycms.repository.ClassroomRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassroomGeneratorService {
	private final ClassroomRepository classroomRepository;
	private static final int FLOORS_COUNT = 3;
	private static final int CLASSROOMS_PER_FLOOR = 10;

	public void generate() {
		List<Classroom> classrooms = new ArrayList<>();

		log.info("Generating classrooms...");
		System.out.println("Generating classrooms...");
		for (int i = 1; i <= FLOORS_COUNT; i++) {
			for (int j = 1; j <= CLASSROOMS_PER_FLOOR; j++) {
				Classroom classroom = new Classroom();
				classroom.setFloor(i);
				classroom.setNumber(String.valueOf(i * 100 + j));
				classrooms.add(classroom);
				log.info("Classroom {} was generated", classroom.getNumber());
			}
		}

		classroomRepository.saveAll(classrooms);
		log.info("Classrooms were generated and added to DB");
		System.out.println("Classrooms were generated and added to DB");
	}
}
