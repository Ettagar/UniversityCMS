package ua.foxminded.universitycms.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Group;
import ua.foxminded.universitycms.model.dto.GroupDto;
import ua.foxminded.universitycms.service.GroupService;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMapper {
	private final StudentMapper studentMapper;
	private final GroupService groupService;

	public GroupDto toDto(Group group) {
		return new GroupDto(
				group.getGroupId(),
				group.getGroupName(),
				group.getStudents().stream()
					.map(studentMapper::toDto)
					.toList());
	}
	
	public List<GroupDto> toDto(List<Group> groups) {
		return groups.stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	public Group toModel(GroupDto groupDto) throws ServiceException {
		if (groupDto.getGroupId() != null) {
			Optional<Group> existingGroup = Optional.ofNullable(groupService.findById(groupDto.getGroupId()));
			if (existingGroup.isPresent()) {
				Group group = existingGroup.get();
				group.setGroupName(groupDto.getGroupName());
				return group;
			}
		}
		return createNewGroup(groupDto);
	}

	private Group createNewGroup(GroupDto groupDto) {
		return new Group(groupDto.getGroupName());
	}
	
	public List<Group> toModel(List<GroupDto> groupsDto) {
		return groupsDto.stream()
			.map(groupDto -> {
				try {
					return toModel(groupDto);
				} catch (ServiceException e) {
					log.error(e.getMessage());
				}
				return null;
			})
			.collect(Collectors.toList());
	}
}
