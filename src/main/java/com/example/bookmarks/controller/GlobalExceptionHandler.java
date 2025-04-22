package com.example.bookmarks.controller;

import com.example.bookmarks.model.Validation;
import com.example.bookmarks.model.ValidationException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  public record ErrorResponse(
      String code, String message, List<Validation.ValidationError> errors) {}

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
    ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", ex.getMessage(), ex.getErrors());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
}
