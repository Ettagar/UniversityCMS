package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.foxminded.universitycms.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>{

	@Query("SELECT COUNT(c) = 0 FROM Course c")
	boolean isEmptyTable();
}
