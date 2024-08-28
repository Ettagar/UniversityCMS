package ua.foxminded.universitycms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.LessonType;
import ua.foxminded.universitycms.repository.LessonTypeRepository;

@Service
@RequiredArgsConstructor
public class LessonTypeService {
    private final LessonTypeRepository lessonTypeRepository;

    @Transactional(readOnly = true)
    public List<LessonType> findAll() {
        return lessonTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public LessonType findById(Long lessonTypeId) throws ServiceException {
		return lessonTypeRepository.findById(lessonTypeId)
				.orElseThrow(() -> new ServiceException("Classrom not found"));
	}
}
