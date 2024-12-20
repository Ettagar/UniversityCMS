package ua.foxminded.universitycms.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.foxminded.universitycms.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.person.dateOfBirth < :date")
	Page<User> findUsersOlderThan(@Param("date") LocalDate date, Pageable pageable);

	@Query("SELECT COUNT(u) = 0 FROM User u")
	boolean checkIfEmptyTable();

	Optional<User> findByUsername(String username);

	@Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
	Optional<User> findByUsernameWithRoles(@Param("username") String username);
}
