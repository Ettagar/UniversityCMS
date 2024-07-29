package ua.foxminded.universitycms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;

@Service
@RequiredArgsConstructor
public class TeacherService {
	private final TeacherRepository teacherRepository;
	private final CourseRepository courseRepository;

	public List<Course> getAvailableCourses(Long teacherId) throws ServiceException {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ServiceException("Teacher not found"));
        List<Course> courses = courseRepository.findAll();
		courses.removeAll(teacher.getCourses());
		return courses;
    }

	public List<Teacher> findAll() {
		return teacherRepository.findAll();
	}

	public Teacher findById(Long id) throws ServiceException {
		return teacherRepository.findById(id).orElseThrow(() -> new ServiceException("Teacher not found"));
	}
}
