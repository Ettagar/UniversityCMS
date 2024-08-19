package ua.foxminded.universitycms.model.dto;

import java.util.List;

public record TeacherDto (
    Long userId,
    String firstName,
    String lastName,
    List<String> courses
) {
}
