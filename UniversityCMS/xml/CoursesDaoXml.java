package ua.foxminded.schoolappspring.dao.xml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import ua.foxminded.schoolappspring.dao.DaoException;

@Component
public class CoursesDaoXml {
	private static final Logger log = LogManager.getLogger(CoursesDaoXml.class.getName());

	public Map<String, String> parseCoursesFromXml(String filename) throws DaoException {
		Map<String, String> courses = new HashMap<>();
		XMLInputFactory factory = XMLInputFactory.newInstance();

		try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(filename);
				BufferedReader courseReader = new BufferedReader(new InputStreamReader(inputStream))) {
			XMLStreamReader reader = factory.createXMLStreamReader(courseReader);
			String courseName = null;
			String courseDescription = null;
			log.info("Parsing courses from file: {}", filename);

			while (reader.hasNext()) {
				int event = reader.next();

				if (event == XMLStreamConstants.START_ELEMENT) {
					String elementName = reader.getLocalName();

					if ("name".equals(elementName)) {
						courseName = reader.getElementText().trim();
					} else if ("description".equals(elementName)) {
						courseDescription = reader.getElementText().trim();
					}
				} else if ((event == XMLStreamConstants.END_ELEMENT) && "course".equals(reader.getLocalName())) {
					courses.put(courseName, courseDescription);
				}
			}
			log.info("Courses were parsed");
		} catch (Exception e) {
			log.error("Error parsing courses from file: {}", filename, e);
			throw new DaoException("Error parsing courses from file: " + filename, e);
		}
		return courses;
	}
}
