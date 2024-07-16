package ua.foxminded.universitycms.service.initializer.generator;

import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ua.foxminded.universitycms.entity.Group;
import ua.foxminded.universitycms.repository.GroupRepository;
import ua.foxminded.universitycms.service.ServiceException;

@Service
public class GroupsGeneratorService {
	private static final Logger log = LogManager.getLogger(GroupsGeneratorService.class.getName());
	private static final int GROUPS_COUNT = 10;
	private static final int GROUPS_PARTS_LENGTH = 2;
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMBERS = "0123456789";
	private static final Random random = new Random();
	private final GroupRepository groupRepository;

	public GroupsGeneratorService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	@Transactional
	public void generate() throws ServiceException {
		if (!groupRepository.checkIfEmptyTable()) {
			System.out.println("Groups already exist");
			log.info("Groups already exist");
			return;
		}
		try {
			Group group = new Group();
			group.setGroupName("NONE");
			groupRepository.save(group);

			for (int i = 0; i < GROUPS_COUNT; i++) {
				Group newGroup = new Group();
				newGroup.setGroupName(generateGroupName());
			    groupRepository.save(newGroup);
			    log.info("Group {} was generated", newGroup.getGroupName());
			}

			List <Group> allGroups = groupRepository.findAll();
			allGroups.forEach(g -> log.info("Group {} is present in DB", g.getGroupName()));
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
