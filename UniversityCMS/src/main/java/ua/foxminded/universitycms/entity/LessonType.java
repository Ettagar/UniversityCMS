package ua.foxminded.universitycms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lesson_types")
public class LessonType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lesson_type_id")
	private Long lessonTypeId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "rated")
	private boolean rated;
}
