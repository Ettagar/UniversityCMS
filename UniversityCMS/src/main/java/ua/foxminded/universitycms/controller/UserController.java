package ua.foxminded.universitycms.controller;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.service.RoleService;
import ua.foxminded.universitycms.service.UserService;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final RoleService roleService;
	
	@GetMapping
	public String listUsers(Model model) {
		List<User> users = userService.findAll();
		users.sort(Comparator.comparing(User::getUserId));
		model.addAttribute("users", users);
		return ("users/users");
	}
	
	@GetMapping("/{id}")
	public String viewUser(@PathVariable Long id, Model model, HttpServletResponse response) throws ServiceException {
		try {
			User user = userService.findById(id);
            model.addAttribute("user", user);
            return "users/user-detail";
        } catch (ServiceException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("errorMessage", e.getMessage());
            return "error/404";
		}		
	}
	
	@GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) throws ServiceException {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        if (user.getPerson() != null && user.getPerson().getDateOfBirth() != null) {
            String formattedDate = user.getPerson().getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            model.addAttribute("formattedDateOfBirth", formattedDate);
        }
        return "users/user-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user, @RequestParam List<Long> roles) throws ServiceException {
        userService.updateUser(id, user, roles);
        return "redirect:/users";
    }
    
    @GetMapping("/user-create")
    public String showCreateForm(Model model) {
    	User user = new User();
    	user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "users/user-create";
    }

    @PostMapping("/user-create")
    public String createUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/users";
    }
}
