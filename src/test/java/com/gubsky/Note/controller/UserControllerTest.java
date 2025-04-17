package com.gubsky.Note.controller;

import com.gubsky.Note.model.User;
import com.gubsky.Note.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class UserControllerTest {

    private UserService userService;
    private UserController controller;
    private Model model;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        controller = new UserController(userService);
        model = new ExtendedModelMap();
    }

    @Test
    void registerForm_shouldPopulateModel() {
        String view = controller.registerForm(model);
        assertEquals("register", view);
        assertTrue(model.containsAttribute("user"));
        assertTrue(model.getAttribute("user") instanceof User);
    }

    @Test
    void registerPost_shouldDelegateAndRedirect() {
        User user = new User();
        String view = controller.register(user);
        assertEquals("redirect:/login", view);
        verify(userService).registerUserWithFirstNote(user);
    }

    @Test
    void loginPage_shouldReturnLoginView() {
        assertEquals("login", controller.loginPage());
    }
}