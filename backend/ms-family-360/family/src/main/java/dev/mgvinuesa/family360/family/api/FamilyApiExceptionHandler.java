package dev.mgvinuesa.family360.family.api;

import dev.mgvinuesa.family360.family.api.v1.model.Problem;
import dev.mgvinuesa.family360.family.application.exception.FamilyMemberNotFoundException;
import dev.mgvinuesa.family360.family.application.exception.FamilyNotFoundException;
import dev.mgvinuesa.family360.family.domain.DomainValidationException;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "dev.mgvinuesa.family360.family.api")
public class FamilyApiExceptionHandler {

    @ExceptionHandler({FamilyNotFoundException.class, FamilyMemberNotFoundException.class})
    ResponseEntity<Problem> handleNotFound(RuntimeException exception) {
        return problem(HttpStatus.NOT_FOUND, "Family resource not found", exception.getMessage());
    }

    @ExceptionHandler({
            DomainValidationException.class,
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class
    })
    ResponseEntity<Problem> handleValidation(Exception exception) {
        return problem(HttpStatus.UNPROCESSABLE_ENTITY, "Request validation failed", exception.getMessage());
    }

    private ResponseEntity<Problem> problem(HttpStatus status, String title, String detail) {
        Problem body = new Problem(title, status.value())
                .type(URI.create("https://family-360.dev/problems/" + status.value()))
                .detail(detail);
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(body);
    }
}
