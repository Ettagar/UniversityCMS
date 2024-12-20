package ua.foxminded.universitycms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.GroupMapper;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.model.Group;
import ua.foxminded.universitycms.model.Student;
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.model.dto.GroupDto;
import ua.foxminded.universitycms.model.dto.StudentDto;
import ua.foxminded.universitycms.service.GroupService;
import ua.foxminded.universitycms.service.RoleService;
import ua.foxminded.universitycms.service.StudentService;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

	private final StudentService studentService;
	private final GroupService groupService;
    private final RoleService roleService;
    private final GroupMapper groupMapper;
    private final StudentMapper studentMapper;
    private final UserTools userTools;

	@GetMapping
	public String listGroups(Model model) {
		List<Group> groups = groupService.findAll();
		model.addAttribute("groups", groups);
		return "groups/groups";
	}

	@GetMapping("/{id}")
	public String viewGroup(@PathVariable Long id, Model model, HttpServletResponse response) {

		try {
			List <StudentDto> ungroupedStudents = studentMapper.toDto(groupService.findById(1L).getStudents());
			model.addAttribute("ungroupedStudents", ungroupedStudents);
			GroupDto group = groupMapper.toDto(groupService.findById(id));
			model.addAttribute("group", group);
			List<Group> groups = groupService.findAll();
			model.addAttribute("groups", groups);
			return ("groups/group-detail");
		} catch (ServiceException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			model.addAttribute("errorMessage", e.getMessage());
			return ("error/404");
		}
	}

	@GetMapping("/my-group")
	public String viewMyGroup(Model model,  HttpServletResponse response) throws ServiceException {
	    User loggedInUser = userTools.getLoggedInUser();
	    if (loggedInUser.getRoles().contains(roleService.getRoleByName("STUDENT"))) {
	        Student student = (Student) loggedInUser;
	        if (student.getGroup() != null) {
	            return "redirect:/groups/" + student.getGroup().getGroupId();
	        } else {
	            model.addAttribute("errorMessage", "No group assigned to you.");
	            return "error/no-group-assigned";
	        }
	    } else {
	    	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	        model.addAttribute("errorMessage", "You are not authorized to view this page. Maybe you are not a student?");
	        return "error/unauthorized";
	    }
	}

	@PostMapping("/changeGroup")
    public String changeStudentGroup(@RequestParam("studentId") Long studentId, @RequestParam("groupId") Long groupId,
                                     Model model,  HttpServletResponse response) {
        try {
            Student student = studentService.findById(studentId);
            Group newGroup = groupService.findById(groupId);

            student.setGroup(newGroup);
            studentService.addStudent(student);

            return "redirect:/groups/" + newGroup.getGroupId();
        } catch (ServiceException e) {
            model.addAttribute("errorMessage", e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "error/404";
        }
    }

	@GetMapping("/group-create")
	public String createGroup(Model model) {
	    model.addAttribute("group", GroupDto.createEmpty());
	    return "groups/group-create";
	}

	@PostMapping("/group-create")
	public String saveGroup(@ModelAttribute("group") GroupDto groupDto) throws ServiceException {
		groupService.addGroup(groupMapper.toModel(groupDto));
		return "redirect:/groups";
	}

	@PostMapping("/delete/{id}")
	public String deleteGroup(@PathVariable Long id, Model model, HttpServletResponse response) {
	    try {
	        Group groupToDelete = groupService.findById(id);
	        Group defaultGroup = groupService.findById(1L);

	        if (groupToDelete.equals(defaultGroup)) {
	            model.addAttribute("errorMessage", "The default group 'NONE' cannot be deleted.");
	            return "redirect:/groups";
	        }

	        List<Student> studentsInGroup = groupToDelete.getStudents();
	        for (Student student : studentsInGroup) {
	            student.setGroup(defaultGroup);
	            studentService.addStudent(student);
	        }

	        groupService.deleteGroup(id);

	        return "redirect:/groups";
	    } catch (ServiceException e) {
	        model.addAttribute("errorMessage", e.getMessage());
	        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	        return "error/404";
	    }
	}
}
