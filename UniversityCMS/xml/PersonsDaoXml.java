package ua.foxminded.schoolappspring.dao.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import ua.foxminded.schoolappspring.dao.DaoException;
import ua.foxminded.schoolappspring.service.dto.Person;

@Component
public class PersonsDaoXml {
	private static final Logger log = LogManager.getLogger(PersonsDaoXml.class);

	public List<Person> parsePersonsFromXml(String filename) throws DaoException {
		log.info("Parsing persons from file: {}", filename);

		try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(filename)) {
			if (inputStream == null) {
				throw new DaoException("File not found: " + filename);
			}

			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
			List<Person> persons = parseXml(reader);
			log.info("Persons were parsed successfully");

			return persons;
		} catch (IOException | XMLStreamException e) {
			log.error("Error reading names from file {}", filename, e);
			throw new DaoException("Error reading names from file " + filename, e);
		}
	}

	private static List<Person> parseXml(XMLStreamReader reader) throws XMLStreamException {
	    List<Person> persons = new ArrayList<>();

	    while (reader.hasNext()) {
	        int event = reader.next();

	        if (isStartElement(event, "person", reader)) {
	            Person person = new Person();
	            parsePersonElement(reader, person);
	            persons.add(person);
	            log.trace("Person {} was parsed", person);
	        }
	    }
	    return persons;
	}

	private static boolean isStartElement(int event, String elementName, XMLStreamReader reader) {
	    return event == XMLStreamConstants.START_ELEMENT && elementName.equals(reader.getLocalName());
	}

	private static void parsePersonElement(XMLStreamReader reader, Person person) throws XMLStreamException {
	    while (reader.hasNext()) {
	        int event = reader.next();

	        if (event == XMLStreamConstants.START_ELEMENT) {
	            String elementName = reader.getLocalName();
	            setPersonProperty(elementName, reader, person);
	        } else if (isEndElement(event, "person", reader)) {
	            break;
	        }
	    }
	}

	private static boolean isEndElement(int event, String elementName, XMLStreamReader reader) {
	    return event == XMLStreamConstants.END_ELEMENT && elementName.equals(reader.getLocalName());
	}

	private static void setPersonProperty(String elementName, XMLStreamReader reader, Person person) throws XMLStreamException {
	    String text = reader.getElementText().trim();

	    switch (elementName) {
	        case "firstName":
	            person.setFirstName(text);
	            break;
	        case "lastName":
	            person.setLastName(text);
	            break;
	        default:
	            log.warn("Unrecognized element: {}", elementName);
	    }
	}
}
