package com.example.bookmarks.controller;

import com.example.bookmarks.model.Validation;
import com.example.bookmarks.model.Validation.ValidationContext;
import com.example.bookmarks.model.ValidationException;
import com.example.bookmarks.persistence.UserDrive;
import com.example.bookmarks.service.DriveService;
import com.example.bookmarks.service.ServiceExceptions;
import com.example.bookmarks.service.ServiceExceptions.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Drives", description = "Operations for managing user virtual file systems")
public class DriveController {

  private final DriveService service;

  public DriveController(DriveService service) {
    this.service = service;
  }

  @PostMapping("/drive/{userId}")
  @Operation(
      summary = "Create a user",
      description = "Initializes a new, empty virtual drive for the specified user.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Successfully created user",
        content = @Content(mediaType = "application/json")),
    @ApiResponse(
        responseCode = "409",
        description = "User already exists",
        content = @Content(mediaType = "text/plain"))
  })
  public ResponseEntity<?> createUser(
      @PathVariable
          @Parameter(description = "The unique ID of the user to initialize", example = "alice")
          String userId) {
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
  @Operation(
      summary = "Fetch a user's virtual drive",
      description =
          "Retrieves the virtual drive structure for a given user ID, including bookmarks and folders.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User's virtual drive found and returned",
        content =
            @Content(
                mediaType = "application/json"
                //                    ,
                //                examples =
                //                    @ExampleObject(
                //                        name = "UserDriveExample",
                //                        summary = "Get Alice's virtual drive",
                //                        value =
                //                            "{ \"userId\": \"alice\", \"data\": { \"contents\": [
                // { \"id\": \"11111111-1111-1111-1111-111111111111\", \"type\": \"folder\",
                // \"name\": \"my-folder\", \"contents\": [ { \"id\":
                // \"aaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee\", \"type\": \"bookmark\", \"name\":
                // \"ChatGPT\", \"url\": \"https://chat.openai.com\" } ] }, { \"id\":
                // \"22222222-2222-2222-2222-222222222222\", \"type\": \"folder\", \"name\":
                // \"inbox\", \"contents\": [] }, { \"id\":
                // \"33333333-3333-3333-3333-333333333333\", \"type\": \"folder\", \"name\":
                // \"trash\", \"contents\": [] } ] }, \"hash\": \"abc123hash==\", \"lastUpdated\":
                // \"2025-04-22T18:00:00Z\" }")
                )),
    @ApiResponse(
        responseCode = "404",
        description = "User not found",
        content = @Content(mediaType = "text/plain"))
  })
  public ResponseEntity<?> getDrive(
      @PathVariable @Parameter(description = "ID of user", example = "alice") String userId) {
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
  @Operation(
      summary = "Update a user's virtual drive",
      description =
          "Replaces the user's virtual drive with an updated structure, if the hash matches",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "New drive structure",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      examples = {
                        @ExampleObject(
                            name = "AliceDrive",
                            summary = "Update Alice's virtual drive",
                            value =
                                "{ \"userId\": \"alice\", \"data\": { \"contents\": [ { \"id\": \"11111111-1111-1111-1111-111111111111\", \"type\": \"folder\", \"name\": \"my-folder\", \"contents\": [ { \"id\": \"aaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee\", \"type\": \"bookmark\", \"name\": \"ChatGPT\", \"url\": \"https://chat.openai.com\" } ] }, { \"id\": \"22222222-2222-2222-2222-222222222222\", \"type\": \"folder\", \"name\": \"inbox\", \"contents\": [] }, { \"id\": \"33333333-3333-3333-3333-333333333333\", \"type\": \"folder\", \"name\": \"trash\", \"contents\": [] } ] }, \"hash\": \"abc123hash==\", \"lastUpdated\": \"2025-04-22T18:00:00Z\" }")
                      })),
      responses = {
        @ApiResponse(responseCode = "200", description = "Successfully updated"),
        @ApiResponse(responseCode = "400", description = "Input validation failed"),
        @ApiResponse(responseCode = "404", description = "No entry for the given user"),
        @ApiResponse(responseCode = "412", description = "Hash mismatch")
      })
  public ResponseEntity<?> putDrive(@RequestBody UserDrive clientCopy) {

    //    UserDrive clientCopy;
    //    try {
    //      ObjectMapper mapper = new ObjectMapper();
    //      clientCopy = mapper.readValue(clientUserDriveJson, UserDrive.class);
    //      // TODO Add exception to method signature and handle with global controller advice?
    //    } catch (JsonProcessingException e) {
    //      throw new RuntimeException(e);
    //    }

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
