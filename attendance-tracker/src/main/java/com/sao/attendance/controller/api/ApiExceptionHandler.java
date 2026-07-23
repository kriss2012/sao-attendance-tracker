package com.sao.attendance.controller.api;

import com.sao.attendance.dto.ErrorResponse;
import com.sao.attendance.exception.AttendanceNotFoundException;
import com.sao.attendance.exception.DuplicateAttendanceException;
import com.sao.attendance.exception.GoogleSheetSyncException;
import com.sao.attendance.exception.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Translates every exception thrown by the {@code controller.api} package
 * into a consistent JSON {@link ErrorResponse} with an appropriate HTTP
 * status, so API consumers never see a raw stack trace.
 */
@RestControllerAdvice(basePackages = "com.sao.attendance.controller.api")
public class ApiExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStudentNotFound(StudentNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AttendanceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAttendanceNotFound(AttendanceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateAttendanceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateAttendanceException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(GoogleSheetSyncException.class)
    public ResponseEntity<ErrorResponse> handleSheetSync(GoogleSheetSyncException ex) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return build(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message, LocalDateTime.now()));
    }
}
