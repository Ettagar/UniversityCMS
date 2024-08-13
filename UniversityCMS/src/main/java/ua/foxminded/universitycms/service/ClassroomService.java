package ua.foxminded.universitycms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.model.Classroom;
import ua.foxminded.universitycms.repository.ClassroomRepository;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }
}
