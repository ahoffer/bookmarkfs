package com.example.bookmarks.service;

public class ServiceExceptions {
  public static class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
      super(message);
    }
  }

  public static class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
      super(message);
    }
  }

  public static class HashMismatchException extends RuntimeException {
    public HashMismatchException(String message) {
      super(message);
    }
  }
}
