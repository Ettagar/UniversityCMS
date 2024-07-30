package ua.foxminded.universitycms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class StudentService {
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;

	@Transactional
	public void addStudent(Student student) {
		studentRepository.save(student);
	}

	@Transactional
	public Student enrollToCourse(Long studentId, Long courseId) throws ServiceException {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ServiceException("Student not found"));
		Course course = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("Course not found"));
		if (!student.getCourses().contains(course)) {
			student.getCourses().add(course);
			studentRepository.save(student);
		} else {
			throw new ServiceException("Course already added");
		}
		return student;
	}

	public List<Course> getAvailableCourses(Long studentId) throws ServiceException {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ServiceException("Student not found"));
		List<Course> courses = courseRepository.findAll();
		courses.removeAll(student.getCourses());
		return courses;
	}

	public List<Course> getCourses(Long studentId) throws ServiceException {
		return studentRepository.findById(studentId).orElseThrow(() -> new ServiceException("Student not found"))
				.getCourses();
	}

	public Student findById(Long id) throws ServiceException {
		return studentRepository.findById(id).orElseThrow(() -> new ServiceException("Student not found"));
	}

	public List<Student> findAll() {
		return studentRepository.findAll();
	}
}
