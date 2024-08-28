package ua.foxminded.universitycms.model.dto;

import java.time.LocalDateTime;

public record ClassroomDto(
	    Long classroomId,
	    String number,
	    Integer floor,
	    boolean occupied,
	    String courseName,
	    TeacherDto teacher,
	    LocalDateTime startTime,
	    LocalDateTime endTime
	) {

	public static ClassroomDto createEmpty() {
		return new ClassroomDto(
				null,  // classroomId
				null,  // number
				null,  // floor
				false,  // occupied
				null,  // courseName
				TeacherDto.createEmpty(),  // teacher
				null,  // startTime
				null  // endTime
			);
	}
}

