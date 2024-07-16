package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.universitycms.entity.Classroom;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Integer>{

}