package ua.foxminded.universitycms.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.foxminded.universitycms.entity.Person;
import ua.foxminded.universitycms.exception.RepositoryException;

@Repository
public class PersonJsonRepository {
    private static final Logger log = LoggerFactory.getLogger(PersonJsonRepository.class);
    private final ObjectMapper objectMapper;

    public PersonJsonRepository() {
        this.objectMapper = new ObjectMapper();
    }

    public List<Person> parsePersonsFromJson(String filename) throws RepositoryException {
        log.info("Parsing persons from file: {}", filename);

        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new RepositoryException("File not found: " + filename);
            }

            List<Person> persons = parseJson(inputStream);
            log.info("Persons were parsed successfully");

            return persons;
        } catch (IOException e) {
            log.error("Error reading persons from file {}", filename, e);
            throw new RepositoryException("Error reading persons from file " + filename, e);
        }
    }

    private List<Person> parseJson(InputStream inputStream) throws IOException {
        List<Person> persons = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(inputStream);
        JsonNode personsNode = rootNode.path("persons");

        if (personsNode.isArray()) {
            for (JsonNode personNode : personsNode) {
                Person person = new Person();
                person.setFirstName(personNode.path("firstName").asText());
                person.setLastName(personNode.path("lastName").asText());
                persons.add(person);
                log.trace("Person {} was parsed", person);
            }
        }
        return persons;
    }
}
