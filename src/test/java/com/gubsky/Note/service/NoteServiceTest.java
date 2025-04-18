package com.gubsky.Note.service;

import com.gubsky.Note.exception.ResourceNotFoundException;
import com.gubsky.Note.model.Note;
import com.gubsky.Note.model.User;
import com.gubsky.Note.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    private Note note1;
    private Note note2;

    @BeforeEach
    void setUp() {
        note1 = new Note();
        note1.setText("Note 1");
        note2 = new Note();
        note2.setText("Note 2");
    }

    @Test
    void getAllNotesForUser_shouldReturnsList() {
        when(noteRepository.findByUserUserId(1L)).thenReturn(List.of(note1, note2));

        List<Note> result = noteService.getAllNotesForUser(1L);

        assertEquals(2, result.size());
        assertTrue(result.contains(note1));
        assertTrue(result.contains(note2));
        verify(noteRepository).findByUserUserId(1L);
    }

    @Test
    void getNoteById_existingId_shouldReturnsNote() {
        when(noteRepository.findById(10L)).thenReturn(Optional.of(note1));

        Note found = noteService.getNoteById(10L);

        assertSame(note1, found);
        verify(noteRepository).findById(10L);
    }

    @Test
    void getNoteById_nonExisting_shouldThrowsException() {
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex =
                assertThrows(ResourceNotFoundException.class, () -> noteService.getNoteById(99L));
        assertEquals("Не найдена заметка с id: 99", ex.getMessage());
        verify(noteRepository).findById(99L);
    }

    @Test
    void saveNote_shouldInvokesRepositorySave() {
        Note newNote = new Note();
        newNote.setText("New note");

        noteService.saveNote(newNote);

        verify(noteRepository).save(newNote);
    }

    @Test
    void deleteNote_existing_shouldDeletes() {
        Long userId = 1L;
        Long noteId = 5L;

        User user = new User();
        user.setUserId(userId);

        Note toDelete = new Note();
        toDelete.setUser(user);

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(toDelete));

        noteService.deleteNote(userId, noteId);

        verify(noteRepository).findById(noteId);
        verify(noteRepository).deleteById(noteId);
    }

    @Test
    void deleteNote_nonExisting_shouldThrowsException() {
        Long userId = 1L;
        Long noteId = 123L;
        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> noteService.deleteNote(userId, noteId)
        );

        assertEquals("Нельзя удалить: заметка с id " + noteId + " не найдена", ex.getMessage());
        verify(noteRepository).findById(noteId);
        verify(noteRepository, never()).deleteById(anyLong());
    }
}