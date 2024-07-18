package ua.foxminded.universitycms.service.initializer.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Group;
import ua.foxminded.universitycms.repository.GroupRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupsGeneratorService {
	private static final int GROUPS_COUNT = 10;
	private static final int GROUPS_PARTS_LENGTH = 2;
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMBERS = "0123456789";
	private static final Random random = new Random();
	private final GroupRepository groupRepository;

	@Transactional
	public void generate() throws ServiceException {
		if (!groupRepository.isEmptyTable()) {
			System.out.println("Groups already exist");
			log.info("Groups already exist");
			return;
		}

		try {
			Group group = new Group();
			group.setGroupName("NONE");
			groupRepository.save(group);

			List<Group> groups = new ArrayList<>();
			for (int i = 0; i < GROUPS_COUNT; i++) {
				Group newGroup = new Group();
				newGroup.setGroupName(generateGroupName());
				groups.add(newGroup);
			    log.info("Group {} was generated", newGroup.getGroupName());
			}

			groupRepository.saveAll(groups);
			System.out.println("Groups were created");
			log.info("Groups were generated");
		} catch (Exception e) {
			log.error("Error generating groups", e);
			throw new ServiceException("Error generating groups", e);
		}
	}

	private static String generateGroupName() {
		StringBuilder name = new StringBuilder();

		for (int i = 0; i < GROUPS_PARTS_LENGTH; i++) {
			name.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
		}
		name.append('-');

		for (int i = 0; i < GROUPS_PARTS_LENGTH; i++) {
			name.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
		}
		return name.toString();
	}
}
