package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.foxminded.universitycms.model.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

	@Query("SELECT COUNT(t) = 0 FROM Teacher t")
	boolean isEmptyTable();
}
