package ua.foxminded.universitycms.repository;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.exception.RepositoryException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CoursesJsonRepository {
    private final ObjectMapper objectMapper;

    public Map<String, String> parseCoursesFromJson(String filename) throws RepositoryException {
        Map<String, String> courses = new HashMap<>();

        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new RepositoryException("File not found: " + filename);
            }
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode coursesNode = rootNode.path("courses");

            log.info("Parsing courses from file: {}", filename);

            if (coursesNode.isArray()) {
                for (JsonNode courseNode : coursesNode) {
                    String courseName = courseNode.path("name").asText();
                    String courseDescription = courseNode.path("description").asText();
                    courses.put(courseName, courseDescription);
                }
            }

            log.info("Courses were parsed");
        } catch (Exception e) {
            log.error("Error parsing courses from file: {}", filename, e);
            throw new RepositoryException("Error parsing courses from file: " + filename, e);
        }

        return courses;
    }
}
