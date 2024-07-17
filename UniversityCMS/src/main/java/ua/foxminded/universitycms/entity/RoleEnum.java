package ua.foxminded.universitycms.entity;

import java.util.List;
import java.util.stream.Stream;

public enum RoleEnum {
    ADMIN("ADMIN", "Administrator"),
    GUEST("GUEST", "Guest"),
    STUDENT("STUDENT", "Student"),
    TEACHER("TEACHER", "Teacher");

    private final String name;
    private final String description;

    RoleEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Role toRole() {
        return new Role(this.getName(), this.getDescription());
    }

    public static List<Role> toRoleList() {
        return Stream.of(RoleEnum.values())
            .map(RoleEnum::toRole)
            .toList();
    }
}
