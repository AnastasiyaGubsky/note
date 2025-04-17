package com.gubsky.Note.security;

import com.gubsky.Note.model.User;
import com.gubsky.Note.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("John");
        user.setPassword("password");
        user.setEmail("test@email.ru");
    }

    @Test
    void loadUserByUsername_existingUser_shouldReturnsUserDetails() {
        when(userRepository.findByUsername("John")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("John");

        assertNotNull(userDetails);
        assertEquals("John", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());

        verify(userRepository).findByUsername("John");
    }

    @Test
    void loadUserByUsername_nonExistingUser_shouldThrowsException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        UsernameNotFoundException e = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown")
        );
        assertEquals("Пользователь не найден: unknown", e.getMessage());

        verify(userRepository).findByUsername("unknown");
    }
}