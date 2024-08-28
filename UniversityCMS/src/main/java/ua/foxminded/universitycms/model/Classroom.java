package ua.foxminded.universitycms.model;

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
@Table(name = "classrooms")
public class Classroom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "classroom_id")
	private Long classroomId;

	@Column(name = "floor")
	private Integer floor;

	@Column(name = "number")
	private String number;
}
