package ua.foxminded.universitycms.util;

import java.util.Arrays;
import java.util.List;

import ua.foxminded.universitycms.model.Teacher;

public class TeacherTestData {
	public List<Teacher> teachers;

	private PersonTestData personTestData = new PersonTestData();

	public void setUp() {
		personTestData.setUp();

		Teacher teacher1 = new Teacher();
		teacher1.setUserId(1L);
		teacher1.setUsername("johnsmith");
		teacher1.setPassword("password");
		teacher1.setPerson(personTestData.persons.get(0));

		Teacher teacher2 = new Teacher();
		teacher2.setUserId(2L);
		teacher2.setUsername("janesmith");
		teacher2.setPassword("password");
		teacher2.setPerson(personTestData.persons.get(1));

		teachers = Arrays.asList(teacher1, teacher2);
	}
}
