package ua.foxminded.universitycms.repository;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.foxminded.universitycms.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {

	@Query("SELECT s FROM Schedule s WHERE s.course.id = :courseId " +
		   "AND s.lessonStart < :end AND s.lessonEnd > :start")
	Set<Schedule> findByCourseIdAndLessonBetween(@Param("courseId") Long courseId,
            									 @Param("start") LocalDateTime start,
            									 @Param("end") LocalDateTime end);


    @Query("SELECT s FROM Schedule s JOIN s.students st WHERE st.id = :studentId " +
           "AND s.lessonStart < :end AND s.lessonEnd > :start")
    Set<Schedule> findByStudentIdAndLessonBetween(@Param("studentId") Long studentId,
                                                  @Param("start") LocalDateTime start,
                                                  @Param("end") LocalDateTime end);

    @Query("SELECT s FROM Schedule s WHERE s.teacher.id = :teacherId " +
           "AND s.lessonStart < :end AND s.lessonEnd > :start")
    Set<Schedule> findByTeacherIdAndLessonBetween(@Param("teacherId") Long teacherId,
                                                  @Param("start") LocalDateTime start,
                                                  @Param("end") LocalDateTime end);

    @Query("SELECT s FROM Schedule s WHERE s.classroom.id = :classroomId " +
           "AND s.lessonStart < :end AND s.lessonEnd > :start")
    Set<Schedule> findByClassroomIdAndLessonBetween(@Param("classroomId") Long classroomId,
                                                    @Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end);

    @Query("SELECT s FROM Schedule s WHERE s.lessonStart < :end AND s.lessonEnd > :start")
    Set<Schedule> findAllByLessonBetween(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);


}
