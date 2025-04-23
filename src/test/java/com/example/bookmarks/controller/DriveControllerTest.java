package com.example.bookmarks.controller;

// TODO Fix this when the interfaces stabilize

/*import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.bookmarks.model.Root;
import com.example.bookmarks.persistence.UserDrive;
import com.example.bookmarks.service.DriveService;
import com.example.bookmarks.service.ServiceExceptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)*/
class DriveControllerTest {

  /*
    private static final String CURRENT_HASH = "abc123";
    private static final String USER_ID = "testUser";

    @InjectMocks private DriveController controller;
    @Mock private DriveService service;

    static ObjectMapper mapper = new ObjectMapper();

    private Root testRoot;

    @BeforeEach
    void setUp() {
      testRoot = Root.createNew();
    }

    @Test
    void getDrive_WhenUserDriveExists_ReturnsOkResponseWithETag() {
      UserDrive testUserDrive = mock(UserDrive.class);
      when(testUserDrive.getData()).thenReturn(testRoot);
      when(testUserDrive.getHash()).thenReturn(CURRENT_HASH);
      when(testUserDrive.getUserId()).thenReturn(USER_ID);
      when(service.getUserDrive(USER_ID)).thenReturn(Optional.of(testUserDrive));

      ResponseEntity<?> response = controller.getDrive(USER_ID);

      assertThat(response).isNotNull();
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getHeaders()).containsKey("ETag");
      assertThat(response.getHeaders().getETag()).isEqualTo('"' + CURRENT_HASH + '"');
      assertThat(response.getHeaders().getFirst("X-User-Id")).isEqualTo(USER_ID);
      assertThat(response.getBody()).isEqualTo(testRoot);
      verify(service).getUserDrive(USER_ID);
    }

    @Test
    void getDrive_WhenUserDriveNotFound_ReturnsNotFoundResponse() {
      when(service.getUserDrive(USER_ID)).thenReturn(Optional.empty());

      ResponseEntity<?> response = controller.getDrive(USER_ID);

      assertThat(response).isNotNull();
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      verify(service).getUserDrive(USER_ID);
    }

    @Test
    void putDrive_WhenHashConflict_ReturnsConflictResponse() throws JsonProcessingException {
      UserDrive inputDrive = new UserDrive(USER_ID, testRoot);

      when(service.putDriveWithFreshnessCheck(new UserDrive(USER_ID, testRoot, "expected-hash")))
          .thenThrow(new ServiceExceptions.HashMismatchException("Hashes don't match"));

      ResponseEntity<?> response =
          controller.putDrive(USER_ID, inputDrive.getData(), "expected-hash"));

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PRECONDITION_FAILED);
      assertThat(response.getBody().toString()).contains("Hash mismatch");
    }

    @Test
    void putDrive_WhenPutSuccessful_ReturnsOkResponse() throws JsonProcessingException {
      UserDrive inputDrive = new UserDrive(USER_ID, testRoot);
      UserDrive returnedDrive = new UserDrive(USER_ID, testRoot, "new-hash");

      when(service.putDriveWithFreshnessCheck(USER_ID, "expected-hash", testRoot))
          .thenReturn(returnedDrive);

      ResponseEntity<?> response =
          controller.putDrive("expected-hash", USER_ID, mapper.writeValueAsString(inputDrive));

      assertThat(response).isNotNull();
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getHeaders().getETag()).isEqualTo("\"new-hash\"");
      assertThat(response.getBody()).isEqualTo(testRoot);
      verify(service).putDriveWithFreshnessCheck(USER_ID, "expected-hash", testRoot);
    }

    @Test
    void putDrive_WhenUserNotFound_ReturnsNotFoundResponse() throws JsonProcessingException {
      UserDrive inputDrive = new UserDrive(USER_ID, testRoot);

      when(service.putDriveWithFreshnessCheck(USER_ID, "expected-hash", testRoot))
          .thenThrow(new ServiceExceptions.UserNotFoundException("User not found"));

      ResponseEntity<?> response =
          controller.putDrive("expected-hash", USER_ID, mapper.writeValueAsString(inputDrive));

      assertThat(response).isNotNull();
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      assertThat(response.getBody().toString()).contains("User not found");
      verify(service).putDriveWithFreshnessCheck(USER_ID, "expected-hash", testRoot);
    }

    @Test
    void createUser_WhenSuccessful_ReturnsCreatedResponse() {
      UserDrive testUserDrive = mock(UserDrive.class);
      when(testUserDrive.getData()).thenReturn(testRoot);
      when(testUserDrive.getHash()).thenReturn(CURRENT_HASH);
      when(testUserDrive.getUserId()).thenReturn(USER_ID);
      when(service.createUser(USER_ID)).thenReturn(testUserDrive);

      ResponseEntity<?> response = controller.createUser(USER_ID);

      assertThat(response).isNotNull();
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertThat(response.getHeaders().getETag()).isEqualTo('"' + CURRENT_HASH + '"');
      assertThat(response.getHeaders().getFirst("X-User-Id")).isEqualTo(USER_ID);
      assertThat(response.getBody()).isEqualTo(testRoot);
      verify(service).createUser(USER_ID);
    }

    @Test
    void createUser_WhenUserExists_ReturnsConflictResponse() {
      when(service.createUser(USER_ID))
          .thenThrow(new IllegalArgumentException("User already exists"));

      ResponseEntity<?> response = controller.createUser(USER_ID);

      assertThat(response).isNotNull();
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
      assertThat(response.getBody().toString()).contains("User already exists");
  }
  */
}
