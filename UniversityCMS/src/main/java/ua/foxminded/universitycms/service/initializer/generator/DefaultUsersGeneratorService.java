package ua.foxminded.universitycms.service.initializer.generator;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Group;
import ua.foxminded.universitycms.model.Person;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.service.GroupService;
import ua.foxminded.universitycms.service.RoleService;
import ua.foxminded.universitycms.service.StudentService;
import ua.foxminded.universitycms.service.TeacherService;
import ua.foxminded.universitycms.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUsersGeneratorService {
	private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final RoleService roleService;
    private final GroupService groupService;
    private final CourseService courseService;

    @Transactional
    public void generate() throws ServiceException {
        log.info("Creating default users...");
        System.out.println("Creating default users...");

        for (DefaultUserCredentials credentials : DefaultUserCredentials.values()) {
            try {
                User user;
                String role = credentials.getRoleName();
                Person person = new Person();
                person.setFirstName(role);
                person.setLastName(role);
                if ("STUDENT".equals(role)) {
                    user = new Student();
                } else if ("TEACHER".equals(role)) {
                    user = new Teacher();
                } else {
                    user = new User();
                }

                user.setPerson(person);
                user.setUsername(credentials.getUsername());
                user.setPassword(passwordEncoder.encode(user.getUsername()));
                user.addRole(roleService.getRoleByName(role));
                user.setEnabled(true);

                // Set specific parameters before saving to DB
                if (user instanceof Student) {
                	studentService.addStudent((Student) user);
                	Group group = groupService.findById(1L);
                	group.addStudent((Student) user);
                } else if (user instanceof Teacher) {
                    teacherService.addTeacher((Teacher) user);
                    courseService.addTeacherToCourse(1L, (Teacher) user);
                } else {
                    userService.addUser(user);
                }

            } catch (Exception e) {
                log.error("Failed to create user with username: {}", credentials.getUsername(), e);
                throw new ServiceException("Failed to create default user", e);
            }
        }

        System.out.println("Default users were created and added to DB");
    }

    public void printDefaultCredentials() {
        System.out.println("You can use for testing purposes:");

        for (DefaultUserCredentials credentials : DefaultUserCredentials.values()) {
            System.out.printf("Default %s account: Login: %s, Password: %s%n",
                credentials.getRoleName(),
                credentials.getUsername(),
                credentials.getPassword());
        }
    }
}
