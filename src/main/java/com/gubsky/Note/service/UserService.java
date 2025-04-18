package com.gubsky.Note.service;

import com.gubsky.Note.model.Note;
import com.gubsky.Note.model.User;
import com.gubsky.Note.repository.NoteRepository;
import com.gubsky.Note.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Бизнес‑логика по работе с пользователями:
 * – регистрация нового пользователя,
 * – создание первой заметки в транзакции,
 * – кодирование пароля.
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, NoteRepository noteRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUserWithFirstNote(User user) {
        User newUser = registerUser(user);

        Note firstNote = new Note();
        firstNote.setText("Добро пожаловать! Это ваша первая заметка.");
        firstNote.setUser(newUser);
        noteRepository.save(firstNote);

        return newUser;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}