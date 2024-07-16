package ua.foxminded.universitycms.service.initializer.generator;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.entity.Course;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.CoursesXmlRepository;
import ua.foxminded.universitycms.repository.RepositoryException;

@Service
public class CoursesGeneratorService {
	private static final Logger log = LogManager.getLogger(CoursesGeneratorService.class);
	private static final String COURSES_FILE = "generated/courses.xml";
	private final CourseRepository courseRepository;
	private final CoursesXmlRepository coursesXmlRepository;

	public CoursesGeneratorService(CourseRepository courseRepository, CoursesXmlRepository coursesXmlRepository) {
		this.courseRepository = courseRepository;
		this.coursesXmlRepository = coursesXmlRepository;
	}

	@Transactional
	public void generate() throws ServiceException {
		if (!courseRepository.checkIfEmptyTable()) {
			System.out.println("Courses already exist");
			log.info("Courses already exist");
			return;
		}
		try {
			log.info("Starting to generate courses...");
			Map<String, String> courses = coursesXmlRepository.parseCoursesFromXml(COURSES_FILE);
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
