package com.gubsky.Note.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Глобальный обработчик исключений:
 * – ловит ResourceNotFoundException и отдаёт страницу not‑found,
 * – ловит прочие исключения и отдаёт общую страницу error.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException e, Model model) {
        logger.warn("Not found: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "not-found";
    }

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception e, Model model) {
        logger.error("Unexpected error occurred", e);
        model.addAttribute("errorMessage", "Что-то пошло не так. Попробуйте позже.");
        return "error";
    }
}