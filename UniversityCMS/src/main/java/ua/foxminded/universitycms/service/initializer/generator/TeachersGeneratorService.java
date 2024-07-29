package ua.foxminded.universitycms.service.initializer.generator;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Person;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.PersonRepository;
import ua.foxminded.universitycms.repository.RoleRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeachersGeneratorService {
	private static final int TEACHERS_COUNT = 40;
	private static final int MIN_AGE = 35;
	private static final int MIN_COURSE_COUNT = 1;
	private static final int MAX_COURSE_COUNT = 3;
	private static final Random random = new Random();
	private static final PrimitiveIterator.OfInt randomCourseCount = random.ints(MIN_COURSE_COUNT, MAX_COURSE_COUNT)
			.iterator();
	private final CourseRepository courseRepository;
	private final TeacherRepository teacherRepository;
	private final RoleRepository roleRepository;
	private final PersonRepository personRepository;
	private final ToolsService toolsService;

	@Transactional
	public void generate() throws Exception {
		LocalDate minYearsAgo = LocalDate.now().minus(Period.ofYears(MIN_AGE));
		Pageable pageable = PageRequest.of(0, TEACHERS_COUNT);
		Page<Person> personsPage = personRepository.findUsersOlderThan(minYearsAgo, pageable);
		List<Person> persons = personsPage.getContent();
		List<Teacher> teachers = new ArrayList<>();

		log.info("Generating teachers...");
		System.out.println("Generating teachers...");
		persons.forEach(person -> {
			Teacher teacher = new Teacher();
			teacher.setPerson(person);
			teacher.setEnabled(true);
			teacher.setUsername(person.getEmail());
			teacher.setPassword(toolsService.generateRandomPassword());
			teacher.addRole(roleRepository.findByName("TEACHER"));
			assignRandomCourses(teacher);
			teachers.add(teacher);
			log.info("Teacher {} {} was generated", person.getFirstName(), person.getLastName());
		});

		try {
			teacherRepository.saveAll(teachers);
		} catch (Exception e) {
			log.error("Error generating teachers", e);
			throw new ServiceException("Error generating teachers", e);
		}

		log.info("Teachers were generated and added to DB");
		System.out.println("Teachers were generated and added to DB");
	}

	 private void assignRandomCourses(Teacher teacher) {
	        int courseCount = randomCourseCount.nextInt();
	        List<Course> allCourses = courseRepository.findAll();
	        List<Course> randomCourses = toolsService.getRandomNElements(allCourses, courseCount);
	        teacher.setCourses(randomCourses);
	    }
}
