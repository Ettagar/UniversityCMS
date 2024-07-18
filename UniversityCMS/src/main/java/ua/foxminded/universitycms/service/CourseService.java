package ua.foxminded.universitycms.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.CourseRepository;

@Service
@RequiredArgsConstructor
public class CourseService {
	private final CourseRepository courseRepository;

	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

	public void addCourse(Course course) {
		courseRepository.save(course);
	}

	public void addCourse(String courseName, String courseDescription) {
		Course course = new Course();
		course.setCourseName(courseName);
		course.setCourseDescription(courseDescription);
		courseRepository.save(course);
	}

	public void addTeacherToCourse(int courseId, Teacher teacher) throws ServiceException {
		Course course = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("Course not found"));
		course.getTeachers().add(teacher);
		courseRepository.save(course);
	}

	public Set<Student> getEnrolledStudents(int courseId) throws ServiceException {
		try {
			Course course = courseRepository.findById(courseId)
					.orElseThrow(() -> new ServiceException("Course not found"));
			return course.getStudents();
		} catch (IllegalArgumentException e) {
			throw new ServiceException("Course_ID cannot be NULL", e);
		}
	}

	public Set<Teacher> getAssignedTeachers(int courseId) throws ServiceException {
		try {
			Course course = courseRepository.findById(courseId)
					.orElseThrow(() -> new ServiceException("Course not found"));
			return course.getTeachers();
		} catch (IllegalArgumentException e) {
			throw new ServiceException("Course_ID cannot be NULL", e);
		}
	}
}
