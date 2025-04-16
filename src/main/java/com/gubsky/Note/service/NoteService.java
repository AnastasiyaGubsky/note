package com.gubsky.Note.service;

import com.gubsky.Note.exception.ResourceNotFoundException;
import com.gubsky.Note.model.Note;
import com.gubsky.Note.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotesForUser(Long userId) {
        return noteRepository.findByUserUserId(userId);
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не найдена заметка с id: " + id));
    }

    public void saveNote(Note note) {
        noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Нельзя удалить: заметка с id " + id + " не найдена"));
        noteRepository.deleteById(id);
    }
}