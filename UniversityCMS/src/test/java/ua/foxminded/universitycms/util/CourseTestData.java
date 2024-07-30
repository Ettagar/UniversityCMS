package ua.foxminded.universitycms.util;

import java.util.Arrays;
import java.util.List;

import ua.foxminded.universitycms.model.Course;

public class CourseTestData {
	public List<Course> courses;

	public void setUp() {
		courses = Arrays.asList(new Course("Course 1", "Description 1"), 
				new Course("Course 2", "Description 2"));
	}
}
