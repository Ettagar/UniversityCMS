package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.universitycms.model.LessonType;

@Repository
public interface LessonTypeRepository extends JpaRepository<LessonType, Long> {

}
