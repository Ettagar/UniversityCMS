package ua.foxminded.universitycms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TeacherService teacherService;
    private final StudentService studentService;

    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Course findById(Long id) throws ServiceException {
        return courseRepository.findById(id).orElseThrow(() -> new ServiceException("Course not found"));
    }

    @Transactional
    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    @Transactional
    public void addCourse(String courseName, String courseDescription) {
        Course course = new Course();
        course.setCourseName(courseName);
        course.setCourseDescription(courseDescription);
        addCourse(course);
    }

    @Transactional
    public void addTeacherToCourse(Long courseId, Teacher teacher) throws ServiceException {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("Course not found"));
        course.getTeachers().add(teacher);
        courseRepository.save(course);
    }

    @Transactional
    public void addStudentToCourse(Long courseId, Long studentId) throws ServiceException {
        Student student = studentService.findById(studentId);
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("Course not found"));
        if (!course.getStudents().contains(student)) {
            course.getStudents().add(student);
            courseRepository.save(course);
        } else {
            throw new ServiceException("Student already enrolled in the course");
        }
    }

    @Transactional(readOnly = true)
    public List<Student> getEnrolledStudents(Long courseId) throws ServiceException {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("Course not found"));
        return course.getStudents();
    }

    public List<Teacher> getAssignedTeachers(Long courseId) throws ServiceException {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("Course not found"));
        return course.getTeachers();
    }

    @Transactional
    public void updateCourse(Course course, List<Long> students, List<Long> teachers) throws ServiceException {
        Course courseToUpdate = courseRepository.findById(course.getCourseId()).orElseThrow(() -> new ServiceException("Course not found"));
        courseToUpdate.setCourseName(course.getCourseName());
        courseToUpdate.setCourseDescription(course.getCourseDescription());
        courseToUpdate.setStudents(course.getStudents());
        courseToUpdate.setTeachers(course.getTeachers());
        courseRepository.save(courseToUpdate);
    }

    @Transactional
    public void removeTeacherFromCourse(Long courseId, Long teacherId) throws ServiceException {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("Course not found"));
        Teacher teacher = course.getTeachers().stream()
                .filter(t -> t.getUserId().equals(teacherId))
                .findFirst()
                .orElseThrow(() -> new ServiceException("Teacher not assigned to course: " + teacherId));
        course.getTeachers().remove(teacher);
		courseRepository.save(course);
    }

    @Transactional
    public void removeStudentFromCourse(Long courseId, Long studentId) throws ServiceException {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("Course not found"));
        Student student = course.getStudents().stream()
                .filter(s -> s.getUserId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new ServiceException("Student not enrolled to course: " + studentId));
        course.getStudents().remove(student);
        courseRepository.save(course);
    }

    @Transactional
    public void addTeacherToCourse(Long courseId, Long teacherId) throws ServiceException {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("Course not found"));
        if (course.getTeachers().stream().anyMatch(t -> t.getUserId().equals(teacherId))) {
            throw new ServiceException("Teacher already assigned to course");
        }
        Teacher teacher = teacherService.findById(teacherId);
        course.getTeachers().add(teacher);
        courseRepository.save(course);
    }
}
