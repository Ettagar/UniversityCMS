package ua.foxminded.universitycms.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	public List<Schedule> findSchedules(Long teacherId, Long studentId, Long classroomId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Schedule> schedules = new ArrayList<>(scheduleRepository.findAllByLessonBetween(startDate, endDate));

        if (teacherId != null) {
            schedules.retainAll(scheduleRepository.findByTeacherIdAndLessonBetween(teacherId, startDate, endDate));
        }
        if (studentId != null) {
            schedules.retainAll(scheduleRepository.findByStudentIdAndLessonBetween(studentId, startDate, endDate));
        }
        if (classroomId != null) {
            schedules.retainAll(scheduleRepository.findByClassroomIdAndLessonBetween(classroomId, startDate, endDate));
        }

        return schedules;
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
	        return classroomMapper.toDto(classrooms, schedules);
	    }
}
