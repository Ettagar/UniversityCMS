package ua.foxminded.universitycms.util;

import java.util.Arrays;
import java.util.List;

import ua.foxminded.universitycms.model.Course;

public class CourseTestData {
	public List<Course> courses;

	public void setUp() {
		Course course1 = new Course();
		course1.setCourseId(1L);
		course1.setCourseName("Course 1");
		course1.setCourseDescription("Description 1");

		Course course2 = new Course();
		course2.setCourseId(2L);
		course2.setCourseName("Course 2");
		course2.setCourseDescription("Description 2");

		courses = Arrays.asList(course1, course2);
	}
}
