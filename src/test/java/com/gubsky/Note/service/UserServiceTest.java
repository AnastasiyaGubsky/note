package com.gubsky.Note.service;

import com.gubsky.Note.model.Note;
import com.gubsky.Note.model.User;
import com.gubsky.Note.repository.NoteRepository;
import com.gubsky.Note.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User inputUser;
    private User savedUser;

    @BeforeEach
    void setUp() {
        inputUser = new User();
        inputUser.setUsername("John");
        inputUser.setEmail("test@email.ru");
        inputUser.setPassword("password");

        savedUser = new User();
        savedUser.setUsername("John");
        savedUser.setEmail("test@email.ru");
        savedUser.setPassword("encodedPassword");

        savedUser.setNotes(null);
    }

    @Test
    void registerUser_shouldEncodesPasswordAndSaves() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(inputUser)).thenReturn(savedUser);

        User result = userService.registerUser(inputUser);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(inputUser);
    }

    @Test
    void registerUserWithFirstNote_shouldSavesUserAndFirstNote() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(inputUser)).thenReturn(savedUser);

        User result = userService.registerUserWithFirstNote(inputUser);

        assertSame(savedUser, result);

        ArgumentCaptor<Note> noteCaptor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).save(noteCaptor.capture());
        Note firstNote = noteCaptor.getValue();

        assertEquals("Добро пожаловать! Это ваша первая заметка.", firstNote.getText());
        assertEquals(savedUser, firstNote.getUser());

        verify(userRepository).save(inputUser);
    }
}