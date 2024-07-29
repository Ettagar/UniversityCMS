package ua.foxminded.universitycms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.model.Group;
import ua.foxminded.universitycms.service.GroupService;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

	private final GroupService groupService;

	@GetMapping
	public String listGroups(Model model) {
		List<Group> groups = groupService.findAll();
		model.addAttribute("groups", groups);
		return "groups/groups";
	}

	@GetMapping("/{id}")
	public String viewGroup(@PathVariable Long id, Model model, HttpServletResponse response) {
		try {
			Group group = groupService.findById(id);
			model.addAttribute("group", group);
			return ("groups/group-detail");
		} catch (ServiceException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			model.addAttribute("errorMessage", e.getMessage());
			return ("error/404");
		}
	}
}
