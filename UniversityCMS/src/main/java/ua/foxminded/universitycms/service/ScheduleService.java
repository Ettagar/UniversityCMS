package ua.foxminded.universitycms.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Classroom;
import ua.foxminded.universitycms.model.Course;
import ua.foxminded.universitycms.model.LessonType;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.Teacher;
import ua.foxminded.universitycms.repository.ClassroomRepository;
import ua.foxminded.universitycms.repository.CourseRepository;
import ua.foxminded.universitycms.repository.LessonTypeRepository;
import ua.foxminded.universitycms.repository.ScheduleRepository;
import ua.foxminded.universitycms.repository.TeacherRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;
	private final CourseRepository courseRepository;
	private final TeacherRepository teacherRepository;
	private final ClassroomRepository classroomRepository;
	private final LessonTypeRepository lessonTypeRepository;

	@Transactional
	public Schedule createSchedule(Long courseId, Long teacherId, Long classroomId, Long lessonTypeId,
			LocalDateTime start, LocalDateTime end) throws ServiceException {
		Course course = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("Course not found"));
		Teacher teacher = teacherRepository.findById(teacherId)
				.orElseThrow(() -> new ServiceException("Teacher not found"));
		Classroom classroom = classroomRepository.findById(classroomId)
				.orElseThrow(() -> new ServiceException("Classroom not found"));
		LessonType lessonType = lessonTypeRepository.findById(lessonTypeId)
				.orElseThrow(() -> new ServiceException("LessonType not found"));

		Set<Student> students = course.getStudents();

		Schedule schedule = new Schedule();
		schedule.setCourse(course);
		schedule.setTeacher(teacher);
		schedule.setClassroom(classroom);
		schedule.setLessonStart(start);
		schedule.setLessonEnd(end);
		schedule.setLessonType(lessonType);
		schedule.setStudents(students);
		return scheduleRepository.save(schedule);
	}

	@Transactional
	public void deleteSchedule(int scheduleId) throws ServiceException {
		Schedule schedule = scheduleRepository.findById(scheduleId)
				.orElseThrow(() -> new ServiceException("Schedule not found"));
		scheduleRepository.delete(schedule);
	}

	public Set<Schedule> findSchedulesByStudent(int studentId, LocalDateTime start, LocalDateTime end) {
		return scheduleRepository.findByStudentIdAndLessonStartBetween(studentId, start, end);
	}

	public Set<Schedule> findSchedulesByTeacher(int teacherId, LocalDateTime start, LocalDateTime end) {
		return scheduleRepository.findByTeacherIdAndLessonStartBetween(teacherId, start, end);
	}
}
