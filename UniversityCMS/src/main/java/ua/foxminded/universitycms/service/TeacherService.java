package ua.foxminded.universitycms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ua.foxminded.universitycms.entity.Course;
import ua.foxminded.universitycms.entity.Teacher;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;

@Service
public class TeacherService {
	private final TeacherRepository teacherRepository;
	private final CourseRepository courseRepository;

	public TeacherService(TeacherRepository teacherRepository, CourseRepository courseRepository) {
		this.teacherRepository = teacherRepository;
		this.courseRepository = courseRepository;
	}

	public List<Course> getAvailableCourses(int teacherId) throws ServiceException {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ServiceException("Teacher not found"));
        List<Course> courses = courseRepository.findAll();
		courses.removeAll(teacher.getCourses());
		return courses;
    }
}
