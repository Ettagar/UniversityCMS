package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.service.RoleService;
import ua.foxminded.universitycms.service.UserService;
import ua.foxminded.universitycms.util.PersonTestData;
import ua.foxminded.universitycms.util.UserTestData;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    private UserTestData userTestData;
    private PersonTestData personTestData;

    @BeforeEach
    void setup() {
        personTestData = new PersonTestData();
        personTestData.setUp();

        userTestData = new UserTestData();
        userTestData.setUp(personTestData);
    }

    @WithMockUser(username = "spring", roles = {"USER"})
    @Test
    void testListUsers() throws Exception {
        when(userService.findAll()).thenReturn(userTestData.users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", userTestData.users));
    }

    @WithMockUser(username = "spring", roles = {"USER"})
    @Test
    void testViewUser() throws Exception {
        when(userService.findById(1L)).thenReturn(userTestData.users.get(0));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/user-detail"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userTestData.users.get(0)));
    }

    @WithMockUser(username = "spring", roles = {"USER"})
    @Test
    void testViewUserNotFound() throws Exception {
        when(userService.findById(4L)).thenThrow(new ServiceException("User not found"));

        mockMvc.perform(get("/users/4"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "User not found"));
    }
}
