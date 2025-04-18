package com.gubsky.Note.service;

import com.gubsky.Note.model.Note;
import com.gubsky.Note.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class NoteServiceCachingTest {


    @TestConfiguration
    @EnableCaching
    static class Config {

        @Bean
        public NoteService noteService(NoteRepository noteRepository) {
            return new NoteService(noteRepository);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("notes", "note");
        }
    }

    @Autowired
    private NoteService noteService;

    @MockBean
    private NoteRepository noteRepository;

    @Test
    void cachingGetAllNotesForUser_shouldCallRepositoryOnce() {
        Long userId = 1L;

        List<Note> notes = List.of(new Note(), new Note());
        when(noteRepository.findByUserUserId(userId)).thenReturn(notes);

        List<Note> result1 = noteService.getAllNotesForUser(userId);
        List<Note> result2 = noteService.getAllNotesForUser(userId);

        assertEquals(result1, result2);
        verify(noteRepository, times(1)).findByUserUserId(userId);
    }
}