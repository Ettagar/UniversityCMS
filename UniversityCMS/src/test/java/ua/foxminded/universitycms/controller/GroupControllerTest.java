package ua.foxminded.universitycms.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.universitycms.exception.ServiceException;
import ua.foxminded.universitycms.mapper.GroupMapper;
import ua.foxminded.universitycms.mapper.StudentMapper;
import ua.foxminded.universitycms.model.User;
import ua.foxminded.universitycms.model.dto.GroupDto;
import ua.foxminded.universitycms.service.GroupService;
import ua.foxminded.universitycms.service.RoleService;
import ua.foxminded.universitycms.service.StudentService;
import ua.foxminded.universitycms.util.GroupTestData;
import ua.foxminded.universitycms.util.PersonTestData;
import ua.foxminded.universitycms.util.StudentTestData;
import ua.foxminded.universitycms.util.UserTestData;

@ActiveProfiles("test")
@WebMvcTest(GroupController.class)
@Import({GroupMapper.class,  StudentMapper.class})
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private StudentService studentService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserTools userTools;

    @Autowired
    private GroupMapper groupMapper;

    private UserTestData userTestData = new UserTestData();
    private PersonTestData personTestData = new PersonTestData();
    private GroupTestData groupTestData = new GroupTestData();
    private StudentTestData studentTestData = new StudentTestData();

    @BeforeEach
    void setup() {
    	personTestData.setUp();
    	userTestData.setUp(personTestData);
		groupTestData.setUp();
		studentTestData.setUp();
    }

    @WithMockUser(username = "spring", roles = {"USER"})
    @Test
    void testListGroups() throws Exception {
        when(groupService.findAll()).thenReturn(groupTestData.groups);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/groups"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("groups", groupTestData.groups));
    }

    @WithMockUser(username = "spring", roles = {"USER"})
    @Test
    void testViewGroup() throws Exception {
        when(groupService.findById(1L)).thenReturn(groupTestData.groups.get(0));
        GroupDto groupDto = groupMapper.toDto(groupTestData.groups.get(0));

        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/group-detail"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attribute("group", groupDto));
    }

    @WithMockUser(username = "spring", roles = {"USER"})
    @Test
    void testViewGroupNotFound() throws Exception {
        when(groupService.findById(1L)).thenThrow(new ServiceException("Group not found"));

        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"));
    }

    @WithMockUser(username = "spring", roles = {"STUDENT"})
    @Test
    void testViewMyGroup() throws Exception {
        when(userTools.getLoggedInUser()).thenReturn(studentTestData.students.get(0));
        when(roleService.getRoleByName("STUDENT")).thenReturn(userTestData.users.get(2)
        		.getRoles().iterator().next());

        mockMvc.perform(get("/groups/my-group"))
                .andExpect(status().is2xxSuccessful());
    }


    @WithMockUser(username = "spring", roles = {"USER"})
    @Test
    void testViewMyGroupNotAuthorized() throws Exception {
        User loggedInUser = new User();
        loggedInUser.setUserId(2L);
        when(userTools.getLoggedInUser()).thenReturn(loggedInUser);

        mockMvc.perform(get("/groups/my-group"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/unauthorized"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @WithMockUser(username = "spring", roles = {"ADMIN"})
    @Test
    void testChangeStudentGroup() throws Exception {
        when(studentService.findById(1L)).thenReturn(studentTestData.students.get(0));
        when(groupService.findById(2L)).thenReturn(groupTestData.groups.get(1));

        mockMvc.perform(post("/groups/changeGroup")
                .param("studentId", "1")
                .param("groupId", "2")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/2"));
    }

    @WithMockUser(username = "spring", roles = {"ADMIN"})
    @Test
    void testChangeStudentGroupNotFound() throws Exception {
        when(studentService.findById(1L)).thenThrow(new ServiceException("Student not found"));
        when(groupService.findById(1L)).thenReturn(groupTestData.groups.get(0));

        mockMvc.perform(post("/groups/changeGroup")
                .param("studentId", "1")
                .param("groupId", "1")
                .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"));
    }
}
