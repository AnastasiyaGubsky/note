package com.gubsky.Note.controller;

import com.gubsky.Note.model.Note;
import com.gubsky.Note.model.User;
import com.gubsky.Note.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteControllerTest {

    private NoteService noteService;
    private NoteController controller;
    private Model model;
    private Authentication authentication;
    private User user;

    @BeforeEach
    void setUp() {
        noteService = mock(NoteService.class);
        controller = new NoteController(noteService);
        model = new ExtendedModelMap();

        user = new User();
        user.setUserId(42L);

        authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void index_shouldReturnIndexViewAndNotes() {
        Note n1 = new Note(); n1.setNoteId(1L);
        Note n2 = new Note(); n2.setNoteId(2L);
        List<Note> notes = Arrays.asList(n1, n2);
        when(noteService.getAllNotesForUser(42L)).thenReturn(notes);

        String view = controller.index(model, authentication);

        assertEquals("index", view);
        assertTrue(model.containsAttribute("notes"));
        assertSame(notes, model.getAttribute("notes"));
        verify(noteService).getAllNotesForUser(42L);
    }

    @Test
    void newNote_shouldReturnFormViewAndEmptyNote() {
        String view = controller.newNote(model);

        assertEquals("note-form", view);
        assertTrue(model.containsAttribute("note"));
        assertTrue(model.getAttribute("note") instanceof Note);
    }

    @Test
    void createOrUpdateNote_shouldSaveNoteAndRedirect() {
        Note input = new Note();
        input.setText("Заметка");
        String view = controller.createOrUpdateNote(input, authentication);

        assertSame(user, input.getUser());
        verify(noteService).saveNote(input);
        assertEquals("redirect:/", view);
    }

    @Test
    void deleteNote_shouldCallServiceAndRedirect() {
        String view = controller.deleteNote(123L, authentication);

        verify(noteService).deleteNote(42L, 123L);
        assertEquals("redirect:/", view);
    }

    @Test
    void editNote_shouldPopulateModelAndReturnFormView() {
        Note found = new Note();
        found.setNoteId(7L);
        when(noteService.getNoteById(7L)).thenReturn(found);

        String view = controller.editNote(7L, model);

        assertEquals("note-form", view);
        assertSame(found, model.getAttribute("note"));
        verify(noteService).getNoteById(7L);
    }
}