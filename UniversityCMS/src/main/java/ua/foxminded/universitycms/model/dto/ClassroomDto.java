package ua.foxminded.universitycms.model.dto;

import java.time.LocalDateTime;

public record ClassroomDto(
	    Long classroomId,
	    String number,
	    int floor,
	    boolean occupied,
	    String courseName,
	    TeacherDto teacher,
	    LocalDateTime startTime,
	    LocalDateTime endTime
	) {
}

