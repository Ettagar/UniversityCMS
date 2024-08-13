package ua.foxminded.universitycms.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.model.enums.RoleEnum;

public class UserTestData {
    public List<User> users;

    public void setUp(PersonTestData personTestData) {

        User user1 = new User();
        user1.setUserId(1L);
        user1.setUsername("johnsmith");
        user1.setPassword("password1");
        user1.setEnabled(true);
        user1.setRoles(Set.of(RoleEnum.ADMIN.toRole(), RoleEnum.USER.toRole()));
        user1.setPerson(personTestData.persons.get(0));

        User user2 = new User();
        user2.setUserId(2L);
        user2.setUsername("janesmith");
        user2.setPassword("password2");
        user2.setEnabled(true);
        user2.setRoles(Set.of(RoleEnum.TEACHER.toRole(), RoleEnum.USER.toRole()));
        user2.setPerson(personTestData.persons.get(1));

        User user3 = new User();
        user3.setUserId(3L);
        user3.setUsername("jimbeam");
        user3.setPassword("password3");
        user3.setEnabled(true);
        user3.setRoles(Set.of(RoleEnum.STUDENT.toRole()));
        user3.setPerson(personTestData.persons.get(2));

        users = new ArrayList<>(List.of(user1, user2, user3));
    }
}
