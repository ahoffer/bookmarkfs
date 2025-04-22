package com.example.bookmarks.controller;

import com.example.bookmarks.model.Validation;
import com.example.bookmarks.model.Validation.ValidationContext;
import com.example.bookmarks.model.ValidationException;
import com.example.bookmarks.persistence.UserDrive;
import com.example.bookmarks.service.DriveService;
import com.example.bookmarks.service.ServiceExceptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriveController {

  private final DriveService service;

  public DriveController(DriveService service) {
    this.service = service;
  }

  @PostMapping("/user/{userId}")
  public ResponseEntity<?> createUser(@PathVariable String userId) {
    try {
      UserDrive userDrive = service.createUser(userId);
      return ResponseEntity.status(HttpStatus.CREATED)
          .eTag('"' + userDrive.getCurrentHash() + '"')
          .header("X-User-Id", userDrive.getUserId())
          .body(userDrive.getData());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: " + e.getMessage());
    }
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getDrive(@PathVariable String userId) {
    return service
        .getUserDrive(userId)
        .map(
            userDrive ->
                ResponseEntity.ok()
                    .eTag('"' + userDrive.getCurrentHash() + '"')
                    .header("X-User-Id", userDrive.getUserId())
                    .body(userDrive.getData()))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/user/{userId}")
  public ResponseEntity<?> putDrive(
      @RequestHeader("If-Match") String expectedHash,
      @PathVariable String userId,
      @RequestBody String userDriveJson) {

    UserDrive userDrive = null;
    try {
      ObjectMapper mapper = new ObjectMapper();
      userDrive = mapper.readValue(userDriveJson, UserDrive.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    ValidationContext result = new Validation().validate(userDrive.getData());
    if (result.hasErrors()) {
      throw new ValidationException(result.getErrors());
    }

    try {
      UserDrive userDrive2 =
          service.putDriveWithFreshnessCheck(userId, expectedHash, userDrive.getData());
      return ResponseEntity.ok()
          .eTag('"' + userDrive.getCurrentHash() + '"')
          .header("X-User-Id", userDrive2.getUserId())
          .body(userDrive2.getData());

    } catch (ServiceExceptions.UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + userId);

    } catch (ServiceExceptions.HashMismatchException e) {
      return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
          .body("Hash mismatch: " + e.getMessage());
    }
  }
}
