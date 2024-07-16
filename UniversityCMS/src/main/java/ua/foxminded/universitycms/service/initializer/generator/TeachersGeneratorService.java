package ua.foxminded.universitycms.service.initializer.generator;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.entity.Course;
import ua.foxminded.universitycms.entity.Person;
import ua.foxminded.universitycms.entity.Teacher;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.PersonRepository;
import ua.foxminded.universitycms.repository.RoleRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;

@Service
public class TeachersGeneratorService {
	private static final Logger log = LogManager.getLogger(TeachersGeneratorService.class.getName());
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

	public TeachersGeneratorService(CourseRepository courseRepository,
			TeacherRepository teacherRepository, RoleRepository roleRepository,
			PersonRepository personRepository, ToolsService toolsService) {
		this.courseRepository = courseRepository;
		this.teacherRepository = teacherRepository;
		this.roleRepository = roleRepository;
		this.personRepository = personRepository;
		this.toolsService = toolsService;

	}
	@Transactional
	public void generate() {
		LocalDate minYearsAgo = LocalDate.now().minus(Period.ofYears(MIN_AGE));
		Pageable pageable = PageRequest.of(0, TEACHERS_COUNT);
		Page <Person> personsPage = personRepository.findUsersOlderThan(minYearsAgo, pageable);
		List<Person> persons = personsPage.getContent();
		log.info("Generating teachers...");
		persons.forEach(person -> {
			Teacher teacher = new Teacher();
			teacher.setPerson(person);
			teacher.setEnabled(true);
			teacher.setUsername(person.getEmail());
			teacher.setPassword(toolsService.generateRandomPassword());
			teacher.addRole(roleRepository.findByName("TEACHER"));
			assignRandomCourses(teacher);
			teacherRepository.save(teacher);
			log.info("Teacher {} {} was generated", person.getFirstName(), person.getLastName());
		});
		log.info("Teachers generation completed");
		System.out.println("Teachers were created");
	}

	 private void assignRandomCourses(Teacher teacher) {
	        int courseCount = randomCourseCount.nextInt();
	        List<Course> allCourses = courseRepository.findAll();
	        List<Course> randomCourses = toolsService.getRandomNElements(allCourses, courseCount);
	        teacher.setCourses(randomCourses);
	    }
}
