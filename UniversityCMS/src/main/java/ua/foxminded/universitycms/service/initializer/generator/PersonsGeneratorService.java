package ua.foxminded.universitycms.service.initializer.generator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Person;
import ua.foxminded.universitycms.repository.PersonJsonRepository;
import ua.foxminded.universitycms.repository.PersonRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonsGeneratorService {
	private static final String PERSONS_FILE = "generated/persons.json";
	private static final int PERSONS_COUNT = 400;
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 60;
	private static final Random random = new Random();
	private final PersonRepository personRepository;
	private final PersonJsonRepository personJsonRepository;

	@Transactional
	public void generate() throws ServiceException {
		if (!personRepository.isEmptyTable()) {
			System.out.println("Persons already exist");
			log.info("Persons already exist");
			return;
		}

		try {
			List<Person> allPersons = new ArrayList<>();
			List<Person> persons = personJsonRepository.parsePersonsFromJson(PERSONS_FILE);

			log.info("Generating persons...");
			System.out.println("Generating persons...");
			for (int i = 0; i < PERSONS_COUNT; i++) {
				Optional<Person> randomPerson1 = persons.stream().skip(random.nextInt(persons.size())).findFirst();
				Optional<Person> randomPerson2 = persons.stream().skip(random.nextInt(persons.size())).findFirst();
				if (randomPerson1.isPresent() && randomPerson2.isPresent()) {
					Person person = new Person();
					person.setFirstName(randomPerson1.get().getFirstName());
					person.setLastName(randomPerson2.get().getLastName());
					person.setDateOfBirth(randomDateOfBirth());
					person.setEmail(person.getFirstName().toLowerCase() + "." + person.getLastName().toLowerCase()
							+ random.nextInt(100 + random.nextInt(900)) + "_"
							+ (person.getDateOfBirth().getYear() % 100) + "@example.com");
					person.setPhoneNumber("+380" + (100000000 + random.nextInt(900000000)));
					allPersons.add(person);
					log.info("Person {} {} {} {} was generated", person.getFirstName(), person.getLastName(),
							person.getDateOfBirth(), person.getEmail());
				} else {
					log.error("Error generating persons");
					throw new ServiceException("Error generating persons");
				}
			}

			personRepository.saveAll(allPersons);

		} catch (Exception e) {
			log.error("Error generating persons", e);
			throw new ServiceException("Error generating persons", e);
		}
		System.out.println("Persons were generated and added to DB");
		log.info("Persons were generated and added to DB");
	}

	private LocalDate randomDateOfBirth() {
		LocalDate today = LocalDate.now();
		LocalDate youngestDate = today.minusYears(MIN_AGE);
		LocalDate oldestDate = today.minusYears(MAX_AGE);

		long startEpochDay = oldestDate.toEpochDay();
		long endEpochDay = youngestDate.toEpochDay();
		long randomDay = startEpochDay + random.nextInt((int) (endEpochDay - startEpochDay));

		return LocalDate.ofEpochDay(randomDay);
	}
}
