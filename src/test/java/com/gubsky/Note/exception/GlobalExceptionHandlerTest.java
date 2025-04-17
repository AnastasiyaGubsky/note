package com.gubsky.Note.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @RestController
    static class TestController {
        @GetMapping("/throw-not-found")
        public String throwNotFound() {
            throw new ResourceNotFoundException("Страница не найдена");
        }

        @GetMapping("/throw-generic")
        public String throwGeneric() {
            throw new RuntimeException("Что-то пошло не так. Попробуйте позже.");
        }
    }

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void handleResourceNotFound_shouldReturnsNotFoundViewWithMessage() throws Exception {
        mockMvc.perform(get("/throw-not-found"))
                .andExpect(status().isOk())
                .andExpect(view().name("not-found"))
                .andExpect(model().attribute("errorMessage", "Страница не найдена"));
    }

    @Test
    void handleAllExceptions_shouldReturnsErrorViewWithGenericMessage() throws Exception {
        mockMvc.perform(get("/throw-generic"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage", "Что-то пошло не так. Попробуйте позже."));
    }
}