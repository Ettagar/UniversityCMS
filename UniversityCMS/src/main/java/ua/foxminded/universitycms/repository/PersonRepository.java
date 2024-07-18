package ua.foxminded.universitycms.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.foxminded.universitycms.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

	@Query("SELECT COUNT(p) = 0 FROM Person p")
	boolean isEmptyTable();

	@Query("SELECT p FROM Person p WHERE p.dateOfBirth < :date")
    Page<Person> findUsersOlderThan(@Param("date") LocalDate date, Pageable pageable);
}
