package ua.foxminded.universitycms.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private List<String> courses;
}
