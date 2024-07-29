package ua.foxminded.universitycms.service.initializer;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.model.LessonType;
import ua.foxminded.universitycms.model.enums.LessonTypeEnum;
import ua.foxminded.universitycms.repository.LessonTypeRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonTypeInitService {
    private final LessonTypeRepository lessonTypeRepository;

    @Transactional
    public void init() {
    	log.info("Creating lesson types...");
    	System.out.println("Creating lesson types...");
        List<LessonType> lessonTypes = LessonTypeEnum.toLessonTypeList();
        lessonTypeRepository.saveAll(lessonTypes);
        log.info("Lesson types were created and added to DB");
        System.out.println("Lesson types were created and added to DB");
    }
}
