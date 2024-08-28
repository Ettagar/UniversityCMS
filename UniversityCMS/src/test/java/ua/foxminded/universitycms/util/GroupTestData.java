package ua.foxminded.universitycms.util;

import java.util.ArrayList;
import java.util.List;

import ua.foxminded.universitycms.model.Group;

public class GroupTestData {
	public List<Group> groups;

    public GroupTestData() {
        groups = new ArrayList<>();
    }

	public void setUp() {
		Group group1 = new Group();
		group1.setGroupId(1L);
		group1.setGroupName("Group 1");

		Group group2 = new Group();
		group2.setGroupId(2L);
		group2.setGroupName("Group 2");

		Group group3 = new Group();
		group3.setGroupId(3L);
		group3.setGroupName("Group 3");

		groups.add(group1);
        groups.add(group2);
        groups.add(group3);
	}
}
