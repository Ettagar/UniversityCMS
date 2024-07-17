package ua.foxminded.universitycms.entity;

import java.util.List;
import java.util.stream.Stream;

public enum LessonTypeEnum {
    LECTURE("Lecture", false),
    PRACTICE("Practice", true),
    LABORATORY("Laboratory", true),
    CONSULTATION("Consultation", false),
    EXAM("Exam", true),
    TEST("Test", true);

    private final String name;
    private final boolean rated;

    LessonTypeEnum(String name, boolean rated) {
        this.name = name;
        this.rated = rated;
    }

    public String getName() {
        return name;
    }

    public boolean isRated() {
        return rated;
    }

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
