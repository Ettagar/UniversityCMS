package ua.foxminded.universitycms.model.dto;

import java.time.LocalDateTime;

public record ClassroomDto(
	    Long classroomId,
	    String number,
	    Integer floor,
	    String courseName,
	    TeacherDto teacher,
	    LocalDateTime startTime,
	    LocalDateTime endTime,
	    boolean occupied
	) {

	public static ClassroomDto createEmpty() {
		return new ClassroomDto(
				null,  // classroomId
				null,  // number
				null,  // floor
				null,  // courseName
				TeacherDto.createEmpty(),  // teacher
				null,  // startTime
				null,  // endTime
				false  // occupied
		);
	}
}
