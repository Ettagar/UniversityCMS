package ua.foxminded.universitycms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Group;
import ua.foxminded.universitycms.repository.GroupRepository;

@Service
@RequiredArgsConstructor
public class GroupService {

	private final GroupRepository groupRepository;

	@Transactional
	public void addGroup(Group group) {
		groupRepository.save(group);
	}

	@Transactional(readOnly = true)
	public Group findById(Long groupId) throws ServiceException {
		return groupRepository.findById(groupId).orElseThrow(() -> new ServiceException("Group not found"));
	}

	@Transactional
	public void deleteGroup(Long groupId) {
		groupRepository.deleteById(groupId);
	}

	@Transactional(readOnly = true)
	public List<Group> findAll() {
		return groupRepository.findAll();
	}
}
