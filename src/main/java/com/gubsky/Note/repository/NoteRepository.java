package com.gubsky.Note.repository;

import com.gubsky.Note.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class NoteRepository {
    private final String FILE_PATH = "notes.json";
    private List<Note> notes = new ArrayList<>();
    private Long nextId = 1L;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try {
                notes = objectMapper.readValue(file, new TypeReference<List<Note>>() {});
                nextId = notes.stream().map(Note::getId).max(Long::compare).orElse(0L) + 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Note defaultNote = new Note(nextId++, "Это первая заметка. Добро пожаловать!");
            notes.add(defaultNote);
            saveToFile();
        }
    }

    @PreDestroy
    public void shutdown() {
        saveToFile();
    }

    private synchronized void saveToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), notes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<Note> findAll() {
        return new ArrayList<>(notes);
    }

    public synchronized Note findById(Long id) {
        return notes.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
    }

    public synchronized void save(Note note) {
        if (note.getId() == null) {
            note.setId(nextId++);
            notes.add(note);
        } else {
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).getId().equals(note.getId())) {
                    notes.set(i, note);
                    break;
                }
            }
        }
        saveToFile();
    }

    public synchronized void delete(Long id) {
        notes.removeIf(n -> n.getId().equals(id));
        saveToFile();
    }
}