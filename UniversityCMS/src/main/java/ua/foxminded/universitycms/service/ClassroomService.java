package ua.foxminded.universitycms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Classroom;
import ua.foxminded.universitycms.repository.ClassroomRepository;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    @Transactional(readOnly = true)
    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }
    
    @Transactional(readOnly = true)
	public Classroom findById(Long classroomId) throws ServiceException {
		return classroomRepository.findById(classroomId)
				.orElseThrow(() -> new ServiceException("Classrom not found"));
	}
}
