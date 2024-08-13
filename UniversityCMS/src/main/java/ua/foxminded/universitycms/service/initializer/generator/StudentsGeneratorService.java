package ua.foxminded.universitycms.service.initializer.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Group;
import ua.foxminded.universitycms.model.Person;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.GroupRepository;
import ua.foxminded.universitycms.repository.PersonRepository;
import ua.foxminded.universitycms.repository.RoleRepository;
import ua.foxminded.universitycms.repository.StudentRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentsGeneratorService {
	private static final int MIN_GROUP_MEMBERS = 10;
	private static final int MAX_GROUP_MEMBERS = 30;
	private static final int MIN_COURSE_COUNT = 1;
	private static final int MAX_COURSE_COUNT = 3;
	private static final Random random = new Random();
	private static final PrimitiveIterator.OfInt randomMembersCount = random
			.ints(MIN_GROUP_MEMBERS, MAX_GROUP_MEMBERS + 1).iterator();
	private static final PrimitiveIterator.OfInt randomCourseCount = random.ints(MIN_COURSE_COUNT, MAX_COURSE_COUNT + 1)
			.iterator();
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;
	private final GroupRepository groupRepository;
	private final PersonRepository personRepository;
	private final RoleRepository roleRepository;
	private final TeacherRepository teacherRepository;
	private final ToolsService toolsService;

	@Transactional
	public void generate() throws ServiceException {
		if (!studentRepository.isEmptyTable()) {
			System.out.println("Students already exist");
			log.info("Students already exist");
			return;
		}

		log.info("Generating students...");
		System.out.println("Generating students...");
		List<Person> teachersPersons = teacherRepository.findAll().stream().map(Teacher::getPerson).toList();
		List<Person> allPersons = personRepository.findAll();
		List<Student> students = new ArrayList<>();

		allPersons.removeAll(teachersPersons);
		for (Person person : allPersons) {
			Student student = new Student();
			student.setPerson(person);
			student.setEnabled(true);
			student.setUsername(person.getEmail());
			student.setPassword(toolsService.generateRandomPassword());
			student.addRole(roleRepository.findByName("STUDENT"));
			student.addRole(roleRepository.findByName("USER"));
			student.setGroup(groupRepository.findByName("NONE"));
			enrollToRandomCourses(student);
			students.add(student);
			log.info("Student {} {} was generated and added to DB", person.getFirstName(), person.getLastName());
		}

		try {
			studentRepository.saveAll(students);
		} catch (Exception e) {
			log.error("Error generating students", e);
			throw new ServiceException("Error generating students", e);
		}
		log.info("Students generation completed and added to DB");

		assignRandomGroups();
		log.info("Students were generated and added to DB");
		System.out.println("Students were generated and added to DB");
	}

	private void enrollToRandomCourses(Student student) {
		int courseCount = randomCourseCount.nextInt();
		List<Course> allCourses = courseRepository.findAll();
		List<Course> randomCourses = toolsService.getRandomNElements(allCourses, courseCount);
		student.setCourses(randomCourses);

	}

	private void assignRandomGroups() {
		List<Group> allGroups = groupRepository.findAll();
		log.info("Found {} groups", allGroups.size());
		List<Student> allStudents = studentRepository.findAll();
		log.info("Found {} students", allStudents.size());
		int nextMemberCount = randomMembersCount.nextInt();

		while (allStudents.size() > nextMemberCount && !allGroups.isEmpty()) {
			List<Student> randomStudents = toolsService.getRandomNElements(allStudents, nextMemberCount);
			Group randomGroup = toolsService.getRandomNElements(allGroups, 1).get(0);
			log.info("Assigning {} students to group {}", randomStudents.size(), randomGroup.getGroupName());
			randomStudents.forEach(student -> {
				student.setGroup(randomGroup);
				studentRepository.save(student);
			});
			allStudents.removeAll(randomStudents);
			allGroups.remove(randomGroup);
			nextMemberCount = randomMembersCount.nextInt();
		}
	}
}
