package com.gubsky.Note.controller;

import com.gubsky.Note.model.Note;
import com.gubsky.Note.model.User;
import com.gubsky.Note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("notes", noteService.getAllNotesForUser(user.getUserId()));
        return "index";
    }

    @GetMapping("/note/new")
    public String newNote(Model model) {
        model.addAttribute("note", new Note());
        return "note-form";
    }

    @PostMapping("/note")
    public String createOrUpdateNote(@ModelAttribute Note note, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        note.setUser(user);

        noteService.saveNote(note);
        return "redirect:/";
    }

    @GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return "redirect:/";
    }

    @GetMapping("/note/{id}")
    public String editNote(@PathVariable Long id, Model model) {
        Note note = noteService.getNoteById(id);
        model.addAttribute("note", note);
        return "note-form";
    }
}