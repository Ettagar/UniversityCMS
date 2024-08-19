package ua.foxminded.universitycms.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.CourseMapper;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.model.dto.CourseDto;
import ua.foxminded.universitycms.service.CourseService;
import ua.foxminded.universitycms.service.UserService;

@Component
@RequiredArgsConstructor
public class UserTools {
	private final UserService userService;
	private final CourseService courseService;
    private final CourseMapper courseMapper;

    public User getLoggedInUser() throws ServiceException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(username);
    }

    public List<CourseDto> assignedCourses(User user) throws ServiceException {
        if (user instanceof Student student) {
            return courseMapper.toDto(student.getCourses());
        } else if (user instanceof Teacher teacher) {
            return courseMapper.toDto(teacher.getCourses());
        } else {
            throw new ServiceException("User is neither a student nor a teacher");
        }
    }

    public List<CourseDto> getNotAssignedCourses(User user) throws ServiceException {
        if (user instanceof Student student) {
            List<Course> enrolledCourses = student.getCourses();
            return courseMapper.toDto(
                courseService.findAll().stream()
                    .filter(course -> !enrolledCourses.contains(course))
                    .toList()
            );
        } else if (user instanceof Teacher teacher) {
            List<Course> assignedCourses = teacher.getCourses();
            return courseMapper.toDto(
                courseService.findAll().stream()
                    .filter(course -> !assignedCourses.contains(course))
                    .toList()
            );
        } else {
            throw new ServiceException("User is neither a student nor a teacher");
        }
    }
}
