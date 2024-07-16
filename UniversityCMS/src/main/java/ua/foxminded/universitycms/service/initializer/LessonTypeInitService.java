package ua.foxminded.universitycms.service.initializer;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import ua.foxminded.universitycms.entity.LessonType;
import ua.foxminded.universitycms.repository.LessonTypeRepository;

@Service
public class LessonTypeInitService {
	private static final Logger log = LogManager.getLogger(LessonTypeInitService.class.getName());
	private final LessonTypeRepository lessonTypeRepository;

	public LessonTypeInitService(LessonTypeRepository lessonTypeRepository) {
		this.lessonTypeRepository = lessonTypeRepository;
	}

	public void init() {
		Map <String, Boolean> lectureTypes = new HashMap<>();
		lectureTypes.put("Lecture", false);
		lectureTypes.put("Practice", true);
		lectureTypes.put("Laboratory", true);
        lectureTypes.put("Consultation", false);
        lectureTypes.put("Exam", true);
        lectureTypes.put("Test", true);

		lectureTypes.forEach((k, v) -> {
			LessonType lessonType = new LessonType();
			lessonType.setName(k);
			lessonType.setRated(v);
			lessonTypeRepository.save(lessonType);
			log.info("Lesson type {} was generated and saved to DB", lessonType.getName());
		});
		log.info("Lesson types were generated");
		System.out.println("Lesson types were created");
	}
}
