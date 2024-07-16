package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.foxminded.universitycms.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>{

	@Query("SELECT COUNT(g) = 0 FROM Group g")
	boolean checkIfEmptyTable();

	@Query("SELECT g FROM Group g WHERE g.groupName = :string")
	Group findByName(String string);

}
