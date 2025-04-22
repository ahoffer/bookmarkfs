package com.example.bookmarks.controller;

import com.example.bookmarks.model.Validation;
import com.example.bookmarks.model.Validation.ValidationContext;
import com.example.bookmarks.model.ValidationException;
import com.example.bookmarks.persistence.UserDrive;
import com.example.bookmarks.service.DriveService;
import com.example.bookmarks.service.ServiceExceptions;
import com.example.bookmarks.service.ServiceExceptions.UserAlreadyExistsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriveController {

  private final DriveService service;

  public DriveController(DriveService service) {
    this.service = service;
  }

  @PostMapping("/drive/{userId}")
  public ResponseEntity<?> createUser(@PathVariable String userId) {
    try {
      UserDrive userDrive = service.createUser(userId);
      return ResponseEntity.status(HttpStatus.CREATED)
          .eTag('"' + userDrive.getHash() + '"')
          .body(userDrive);
    } catch (UserAlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }

  @GetMapping("/drive/{userId}")
  public ResponseEntity<?> getDrive(@PathVariable String userId) {
    return service
        .getUserDrive(userId)
        .map(
            userDrive ->
                ResponseEntity.ok()
                    .eTag(userDrive.getHash())
                    .header("X-User-Id", userDrive.getUserId())
                    .body(userDrive))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/drive")
  public ResponseEntity<?> putDrive(@RequestBody String clientUserDriveJson) {

    UserDrive clientCopy;
    try {
      ObjectMapper mapper = new ObjectMapper();
      clientCopy = mapper.readValue(clientUserDriveJson, UserDrive.class);
      // TODO Add exception to method signature and handle with global controller advice?
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    ValidationContext result = new Validation().validate(clientCopy.getData());
    if (result.hasErrors()) {
      throw new ValidationException(result.getErrors());
    }

    // TODO Move to global controller advice?
    try {
      UserDrive updateUserDrive = service.putDriveWithFreshnessCheck(clientCopy);
      return ResponseEntity.ok()
          .eTag('"' + clientCopy.getHash() + '"')
          .header("X-User-Id", updateUserDrive.getUserId())
          .body(updateUserDrive.getData());

    } catch (ServiceExceptions.UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

    } catch (ServiceExceptions.HashMismatchException e) {
      return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
    }
  }
}
