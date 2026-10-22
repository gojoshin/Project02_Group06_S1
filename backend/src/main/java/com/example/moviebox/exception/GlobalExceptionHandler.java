package com.example.moviebox.exception;

import com.example.moviebox.dto.Problem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final MediaType PROBLEM_JSON = MediaType.parseMediaType("application/problem+json");

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Problem> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        Problem problem = new Problem("about:blank", "Not Found", HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(PROBLEM_JSON).body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Problem> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        Problem problem = new Problem("about:blank", "Bad Request", HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(PROBLEM_JSON).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Problem problem = new Problem("about:blank", "Bad Request", HttpStatus.BAD_REQUEST.value(), "Validation failed for request", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(PROBLEM_JSON).body(problem);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Problem> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        int status = ex.getStatusCode().value();
        HttpStatus httpStatus = HttpStatus.valueOf(status);
        String detail = ex.getReason() != null ? ex.getReason() : httpStatus.getReasonPhrase();
        Problem problem = new Problem("about:blank", httpStatus.getReasonPhrase(), status, detail, request.getRequestURI());
        return ResponseEntity.status(status).contentType(PROBLEM_JSON).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleInternalError(Exception ex, HttpServletRequest request) {
        Problem problem = new Problem("about:blank", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(PROBLEM_JSON).body(problem);
    }
}
