package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.foxminded.universitycms.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Query("SELECT COUNT(s) = 0 FROM Student s")
	boolean checkIfEmptyTable();
}
