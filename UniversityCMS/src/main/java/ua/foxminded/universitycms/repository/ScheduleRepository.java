package ua.foxminded.universitycms.repository;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.foxminded.universitycms.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	@Query("SELECT s FROM Schedule s JOIN s.students st WHERE st.id = :studentId AND s.lessonStart BETWEEN :start AND :end")
	Set<Schedule> findByStudentIdAndLessonStartBetween(@Param("studentId") Long studentId,
			@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT s FROM Schedule s WHERE s.teacher.id = :teacherId AND s.lessonStart BETWEEN :start AND :end")
	Set<Schedule> findByTeacherIdAndLessonStartBetween(@Param("teacherId") Long teacherId,
			@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	 @Query("SELECT s FROM Schedule s WHERE s.classroom.id = :classroomId AND s.lessonStart BETWEEN :start AND :end")
	 Set<Schedule> findByClassroomIdAndLessonStartBetween(@Param("classroomId") Long classroomId,
			 @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	 @Query("SELECT s FROM Schedule s WHERE s.lessonStart BETWEEN :start AND :end")
	 Set<Schedule> findAllByLessonStartBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
