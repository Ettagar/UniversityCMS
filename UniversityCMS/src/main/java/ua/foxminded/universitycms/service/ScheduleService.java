package ua.foxminded.universitycms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.ClassroomMapper;
import ua.foxminded.universitycms.model.Classroom;
import ua.foxminded.universitycms.model.Schedule;
import ua.foxminded.universitycms.model.dto.ClassroomDto;
import ua.foxminded.universitycms.model.dto.ScheduleDto;
import ua.foxminded.universitycms.repository.ClassroomRepository;
import ua.foxminded.universitycms.repository.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;
	private final ClassroomRepository classroomRepository;
	private final ClassroomMapper classroomMapper;

	@Transactional
	public void addSchedule(Schedule schedule) {
		 scheduleRepository.save(schedule);
	}

	@Transactional(readOnly = true)
	public List<Schedule> findSchedules(Long courseId, Long teacherId, Long studentId,
			Long classroomId, LocalDateTime startDate, LocalDateTime endDate) {
	    Specification<Schedule> spec = Specification.where(null);
	    // Add date range filter
	    if (startDate != null && endDate != null) {
	        spec = spec.and((root, query, criteriaBuilder) ->
	            criteriaBuilder.and(
	            		criteriaBuilder.lessThan(root.get("lessonStart"), endDate), // Start before the new schedule's end
	            		criteriaBuilder.greaterThan(root.get("lessonEnd"), startDate) // End after the new schedule's start
	            )
	        );
	    }
	    // Add course filter
	    if (courseId != null) {
	        spec = spec.and((root, query, criteriaBuilder) ->
	            criteriaBuilder.equal(root.get("course").get("id"), courseId));
	    }
	    // Add teacher filter
	    if (teacherId != null) {
	        spec = spec.and((root, query, criteriaBuilder) ->
	            criteriaBuilder.equal(root.get("teacher").get("id"), teacherId));
	    }
	    // Add student filter
	    if (studentId != null) {
	        spec = spec.and((root, query, criteriaBuilder) ->
	            criteriaBuilder.equal(root.join("students").get("id"), studentId));
	    }
	    // Add classroom filter
	    if (classroomId != null) {
	        spec = spec.and((root, query, criteriaBuilder) ->
	            criteriaBuilder.equal(root.get("classroom").get("id"), classroomId));
	    }

	    return scheduleRepository.findAll(spec);
	}

	@Transactional
	public void deleteSchedule(Long scheduleId) throws ServiceException {
		Schedule schedule = scheduleRepository.findById(scheduleId)
				.orElseThrow(() -> new ServiceException("Schedule not found"));
		scheduleRepository.delete(schedule);
	}

	public Set<Schedule> findSchedulesByStudent(Long studentId, LocalDateTime start, LocalDateTime end) {
		return scheduleRepository.findByStudentIdAndLessonBetween(studentId, start, end);
	}

	public Set<Schedule> findSchedulesByTeacher(Long teacherId, LocalDateTime start, LocalDateTime end) {
		return scheduleRepository.findByTeacherIdAndLessonBetween(teacherId, start, end);
	}

	public Schedule findById(Long scheduleId) throws ServiceException {
		return scheduleRepository.findById(scheduleId)
				.orElseThrow(() -> new ServiceException("Schedule not found"));
	}

	 @Transactional(readOnly = true)
	    public List<ClassroomDto> getAllClassroomsWithOccupancy(List<ScheduleDto> schedules) {
	        List<Classroom> classrooms = classroomRepository.findAll();
	        Map<Long, ScheduleDto> scheduleMap = schedules.stream()
	                .collect(Collectors.toMap(schedule -> schedule.classroom().getClassroomId(), schedule -> schedule));
	        return classroomMapper.toDto(classrooms, scheduleMap);
	    }

	public List<Schedule> findAll() {
		return scheduleRepository.findAll();
	}
}
