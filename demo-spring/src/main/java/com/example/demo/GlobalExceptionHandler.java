package com.example.demo;

import com.github.danitutu.painlessjavavalidator.ValidationException;
import com.github.danitutu.painlessjavavalidator.Violation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ValidationException.class)
    protected ResponseEntity<Object> handleValidationException(
            ValidationException ex, WebRequest request) {
        List<Violation> violations = ex.getViolations();
        Map<String, Object> map = new HashMap<>();
        map.put("type", "VALIDATION-EXCEPTION");
        map.put("validationResult", violations);
        return handleExceptionInternal(
                ex,
                map,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }
}
