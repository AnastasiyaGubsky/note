package com.gubsky.Note.controller;

import com.gubsky.Note.model.Note;
import com.gubsky.Note.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("notes", noteRepository.findAll());
        return "index";
    }

    @GetMapping("/note/new")
    public String newNote(Model model) {
        model.addAttribute("note", new Note());
        return "note-form";
    }

    @GetMapping("/note/{id}")
    public String editNote(@PathVariable Long id, Model model) {
        Note note = noteRepository.findById(id);
        if (note == null) {
            return "redirect:/";
        }
        model.addAttribute("note", note);
        return "note-form";
    }

    @PostMapping("/note")
    public String saveNote(@ModelAttribute Note note) {
        noteRepository.save(note);
        return "redirect:/";
    }

    @GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable Long id) {
        noteRepository.delete(id);
        return "redirect:/";
    }
}