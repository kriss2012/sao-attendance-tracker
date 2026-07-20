package com.sao.attendance.controller.web;

import com.sao.attendance.exception.AttendanceNotFoundException;
import com.sao.attendance.exception.DuplicateAttendanceException;
import com.sao.attendance.exception.GoogleSheetSyncException;
import com.sao.attendance.exception.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Catches exceptions raised by {@code controller.web} handlers and renders
 * the themed error view instead of the default whitelabel error page.
 */
@ControllerAdvice(basePackages = "com.sao.attendance.controller.web")
public class WebExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleStudentNotFound(StudentNotFoundException ex, Model model) {
        return renderError(model, "PLAYER NOT FOUND", ex.getMessage());
    }

    @ExceptionHandler(AttendanceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAttendanceNotFound(AttendanceNotFoundException ex, Model model) {
        return renderError(model, "RECORD NOT FOUND", ex.getMessage());
    }

    @ExceptionHandler(DuplicateAttendanceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicate(DuplicateAttendanceException ex, Model model) {
        return renderError(model, "ALREADY LOGGED", ex.getMessage());
    }

    @ExceptionHandler(GoogleSheetSyncException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleSheetSync(GoogleSheetSyncException ex, Model model) {
        return renderError(model, "CARDINAL SYSTEM LINK FAILED", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex, Model model) {
        return renderError(model, "SYSTEM ERROR", "An unexpected error occurred: " + ex.getMessage());
    }

    private String renderError(Model model, String title, String message) {
        model.addAttribute("errorTitle", title);
        model.addAttribute("errorMessage", message);
        return "error";
    }
}
