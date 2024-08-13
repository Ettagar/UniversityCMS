package ua.foxminded.universitycms.util;

import java.time.LocalDate;
import java.util.List;

import ua.foxminded.universitycms.model.Person;

public class PersonTestData {
	public List<Person> persons;

	public void setUp() {
		Person person1 = new Person();
		person1.setPersonId(1L);
		person1.setFirstName("John");
		person1.setLastName("Smith");
		person1.setDateOfBirth(LocalDate.of(1980, 1, 1));
		person1.setEmail("john.smith@example.com");
		person1.setPhoneNumber("1234567890");

		Person person2 = new Person();
		person2.setPersonId(2L);
		person2.setFirstName("Jane");
		person2.setLastName("Smith");
		person2.setDateOfBirth(LocalDate.of(1985, 2, 2));
		person2.setEmail("jane.smith@example.com");
		person2.setPhoneNumber("0987654321");

		Person person3 = new Person();
		person3.setPersonId(3L);
		person3.setFirstName("Jim");
		person3.setLastName("Beam");
		person3.setDateOfBirth(LocalDate.of(1975, 3, 3));
		person3.setEmail("jim.beam@example.com");
		person3.setPhoneNumber("1230933567");

		persons = List.of(person1, person2, person3);
	}
}
