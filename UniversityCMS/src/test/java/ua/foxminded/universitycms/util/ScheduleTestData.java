package ua.foxminded.universitycms.util;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.mapper.CourseMapper;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.mapper.TeacherMapper;
import ua.foxminded.universitycms.model.Classroom;
import ua.foxminded.universitycms.model.LessonType;
import ua.foxminded.universitycms.model.dto.ScheduleDto;
import ua.foxminded.universitycms.model.dto.StudentDto;

@Component
@RequiredArgsConstructor
public class ScheduleTestData {

    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;
    private final TeacherMapper teacherMapper;

    private CourseTestData courseTestData = new CourseTestData();
    private TeacherTestData teacherTestData = new TeacherTestData();
    private StudentTestData studentTestData = new StudentTestData();

    public List<ScheduleDto> schedules;

    public void setUp() {
        courseTestData.setUp();
        teacherTestData.setUp();
        studentTestData.setUp();

        Classroom classroom = new Classroom();
        classroom.setClassroomId(1L);
        classroom.setNumber("101");
        classroom.setFloor(1);

        LessonType lessonType = new LessonType();
        lessonType.setLessonTypeId(1L);
        lessonType.setName("Lecture");

        List<StudentDto> studentDtos = studentMapper.toDto(studentTestData.students);

        ScheduleDto schedule1 = new ScheduleDto(
            1L,
            courseMapper.toDto(courseTestData.courses.get(0)),
            teacherMapper.toDto(teacherTestData.teachers.get(0)),
            classroom,
            LocalDateTime.of(2024, 9, 1, 10, 0),
            LocalDateTime.of(2024, 9, 1, 12, 0),
            lessonType,
            List.of(studentDtos.get(0))
        );

        ScheduleDto schedule2 = new ScheduleDto(
            2L,
            courseMapper.toDto(courseTestData.courses.get(1)),
            teacherMapper.toDto(teacherTestData.teachers.get(1)),
            classroom,
            LocalDateTime.of(2024, 9, 2, 14, 0),
            LocalDateTime.of(2024, 9, 2, 16, 0),
            lessonType,
            List.of(studentDtos.get(1), studentDtos.get(2))
        );

        schedules = List.of(schedule1, schedule2);
    }
}
