package ua.foxminded.universitycms.service.initializer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.entity.LessonType;
import ua.foxminded.universitycms.entity.LessonTypeEnum;
import ua.foxminded.universitycms.repository.LessonTypeRepository;

@Service
@RequiredArgsConstructor
public class LessonTypeInitService {
	private static final Logger log = LoggerFactory.getLogger(LessonTypeInitService.class.getName());
    private final LessonTypeRepository lessonTypeRepository;

    @Transactional
    public void init() {
        List<LessonType> lessonTypes = LessonTypeEnum.toLessonTypeList();
        lessonTypeRepository.saveAll(lessonTypes);
        log.info("Lesson types were created");
        System.out.println("Lesson types were created");
    }
}
