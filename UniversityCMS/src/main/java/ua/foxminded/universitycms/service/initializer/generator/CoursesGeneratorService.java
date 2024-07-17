package ua.foxminded.universitycms.service.initializer.generator;

import java.util.Map;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.entity.Course;
import ua.foxminded.universitycms.exception.RepositoryException;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.CoursesJsonRepository;

@Service
@RequiredArgsConstructor
public class CoursesGeneratorService {
	private static final Logger log = LoggerFactory.getLogger(CoursesGeneratorService.class);
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
			Map<String, String> courses = coursesJsonRepository.parseCoursesFromJson(COURSES_FILE);
			courses.forEach((name, description) -> {
				Course course = new Course();
				course.setCourseName(name);
				course.setCourseDescription(description);
				try {
					courseRepository.save(course);
					log.info("Successfully created course: {}", name);
				} catch (Exception e) {
					log.error("Error creating course: ", e);
				}
			});
		} catch (RepositoryException e) {
			log.error("Error generating courses", e);
			throw new ServiceException("Error generating courses", e);
		}
		System.out.println("Courses were created");
		log.info("Courses were generated");
	}
}
