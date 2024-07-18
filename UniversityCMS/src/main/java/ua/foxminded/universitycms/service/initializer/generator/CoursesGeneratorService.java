package ua.foxminded.universitycms.service.initializer.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.CoursesJsonRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoursesGeneratorService {
	private static final String COURSES_FILE = "generated/courses.json";
	private final CourseRepository courseRepository;
	private final CoursesJsonRepository coursesJsonRepository;

	@Transactional
	public void generate() throws ServiceException {
		if (!courseRepository.isEmptyTable()) {
			System.out.println("Courses already exist");
			log.info("Courses already exist");
			return;
		}

		try {
			log.info("Starting to generate courses...");
			List<Course> allCourses = new ArrayList<>();
			Map<String, String> courses = coursesJsonRepository.parseCoursesFromJson(COURSES_FILE);

			courses.forEach((name, description) -> {
				Course course = new Course();
				course.setCourseName(name);
				course.setCourseDescription(description);
				allCourses.add(course);
				log.info("Successfully created course: {}", name);
			});

			courseRepository.saveAll(allCourses);
		} catch (Exception e) {
			log.error("Error generating courses", e);
			throw new ServiceException("Error generating courses", e);
		}

		System.out.println("Courses were created");
		log.info("Courses were generated");
	}
}
