package com.gubsky.Note.service;

import com.gubsky.Note.exception.ResourceNotFoundException;
import com.gubsky.Note.model.Note;
import com.gubsky.Note.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Cacheable(value = "notes", key = "#userId")
    public List<Note> getAllNotesForUser(Long userId) {
        return noteRepository.findByUserUserId(userId);
    }

    @Cacheable(value = "note", key = "#id")
    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не найдена заметка с id: " + id));
    }

    @Caching(evict = {
            @CacheEvict(value = "notes", key = "#note.user.userId"),
            @CacheEvict(value = "note",  key = "#note.noteId")
    })
    public void saveNote(Note note) {
        noteRepository.save(note);
    }

    @Caching(evict = {
            @CacheEvict(value = "notes", key = "#userId"),
            @CacheEvict(value = "note",  key = "#noteId")
    })
    public void deleteNote(Long userId, Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Нельзя удалить: заметка с id " + noteId + " не найдена"));

        if (!note.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("Нельзя удалить чужую заметку");
        }

        noteRepository.deleteById(noteId);
    }
}