package com.example.bookmarks.model;

import java.util.List;

public class ValidationException extends RuntimeException {
  private final List<Validation.ValidationError> errors;

  public ValidationException(List<Validation.ValidationError> errors) {
    super(buildMessage(errors));
    this.errors = List.copyOf(errors);
  }

  public List<Validation.ValidationError> getErrors() {
    return errors;
  }

  static String buildMessage(List<Validation.ValidationError> errors) {
    if (errors == null || errors.isEmpty()) {
      return "Validation failed with no details.";
    }
    return "Validation failed:\n"
        + errors.stream()
            .map(e -> e.path() + ": " + e.message())
            .reduce((a, b) -> a + "\n" + b)
            .orElse("Unknown validation error.");
  }
}
