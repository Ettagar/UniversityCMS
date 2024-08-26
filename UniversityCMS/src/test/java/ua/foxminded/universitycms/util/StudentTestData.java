package ua.foxminded.universitycms.util;

import java.util.Arrays;
import java.util.List;

import ua.foxminded.universitycms.model.Student;

public class StudentTestData {
	private CourseTestData courseTestData = new CourseTestData();
	private PersonTestData personTestData = new PersonTestData();
	private GroupTestData groupTestData = new GroupTestData();
	
	public List<Student> students;

	public void setUp() {
		courseTestData.setUp();
		personTestData.setUp();
		groupTestData.setUp();

		Student student1 = new Student();
		student1.setUserId(1L);
		student1.setUsername("johnsmith");
		student1.setPassword("password");
		student1.setGroup(groupTestData.groups.get(0));
		student1.setPerson(personTestData.persons.get(0));

		Student student2 = new Student();
		student2.setUserId(2L);
		student2.setUsername("janesmith");
		student2.setPassword("password");
		student2.setGroup(groupTestData.groups.get(0));
		student2.setPerson(personTestData.persons.get(1));

		Student student3 = new Student();
		student3.setUserId(3L);
		student3.setUsername("jimbeam");
		student3.setPassword("password");
		student3.setGroup(groupTestData.groups.get(0));
		student3.setPerson(personTestData.persons.get(2));

		students = Arrays.asList(student1, student2, student3);
		students.forEach(student -> student.setCourses(courseTestData.courses));
	}
}
