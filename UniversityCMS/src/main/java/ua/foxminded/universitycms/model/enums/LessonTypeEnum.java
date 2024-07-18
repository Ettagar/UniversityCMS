package ua.foxminded.universitycms.model.enums;

import java.util.List;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ua.foxminded.universitycms.model.LessonType;

@Getter
@AllArgsConstructor
public enum LessonTypeEnum {
    LECTURE("Lecture", false),
    PRACTICE("Practice", true),
    LABORATORY("Laboratory", true),
    CONSULTATION("Consultation", false),
    EXAM("Exam", true),
    TEST("Test", true);

    private final String name;
    private final boolean rated;

    public LessonType toLessonType() {
        LessonType lessonType = new LessonType();
        lessonType.setName(this.getName());
        lessonType.setRated(this.isRated());
        return lessonType;
    }

    public static List<LessonType> toLessonTypeList() {
        return Stream.of(LessonTypeEnum.values())
            .map(LessonTypeEnum::toLessonType)
            .toList();
    }
}
