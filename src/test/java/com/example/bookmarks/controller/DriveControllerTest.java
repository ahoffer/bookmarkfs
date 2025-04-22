package com.example.bookmarks.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.bookmarks.model.Root;
import com.example.bookmarks.persistence.UserDrive;
import com.example.bookmarks.service.DriveService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DriveControllerTest {
  private static final String CURRENT_HASH = "abc123";
  private static final String USER_ID = "testUser";
  @InjectMocks private DriveController controller;
  @Mock private DriveService service;
  private UserDrive testUserDrive;

  @Test
  void getBookmarkTree_WhenUserDriveExists_ReturnsOkResponseWithETag() {
    Root root = Root.createNew(); // Use a single instance for both stubbing and assertion

    when(testUserDrive.getCurrentHash()).thenReturn(CURRENT_HASH);
    when(testUserDrive.getData()).thenReturn(root);
    when(service.getUserDrive(USER_ID)).thenReturn(Optional.of(testUserDrive));
    ResponseEntity<?> response = controller.getBookmarkTree(USER_ID);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getHeaders()).containsKey("ETag");
    assertThat(response.getHeaders().getETag()).isEqualTo('"' + CURRENT_HASH + '"');
    assertThat(response.getBody()).isEqualTo(root); // Same instance as above
    verify(service).getUserDrive(USER_ID);
  }


  @Test
  void getBookmarkTree_WhenUserDriveNotFound_ReturnsNotFoundResponse() {
    when(service.getUserDrive(USER_ID)).thenReturn(Optional.empty());
    ResponseEntity<?> response = controller.getBookmarkTree(USER_ID);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    verify(service).getUserDrive(USER_ID);
  }

  @BeforeEach
  void setUp() {
    testUserDrive = mock(UserDrive.class);
  }

  @Test
  void putBookmarkTree_WhenHashConflict_ReturnsConflictResponse() {
    // Arrange
    String userId = "user-123";
    String expectedHash = "expected-hash";
    Root root = Root.createNew();

    doThrow(new IllegalArgumentException("Hash mismatch"))
            .when(service).updateTreeWithHashCheck(userId, expectedHash, root);

    DriveController controller = new DriveController(service);

    ResponseEntity<?> response = controller.putBookmarkTree(userId, expectedHash, root);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody()).isEqualTo("Conflict: Hash mismatch");
  }


  @Test
  void updateBookmarkTree_WhenPutSuccessful_ReturnsNoContent() {
    String expectedHash = "expected-hash";
    Root root = Root.createNew(); // Create it once

    doNothing().when(service).updateTreeWithHashCheck(USER_ID, expectedHash, root);

    ResponseEntity<?> response = controller.putBookmarkTree(USER_ID, expectedHash, root);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    verify(service).updateTreeWithHashCheck(USER_ID, expectedHash, root);
  }


  @Test
  void putBookmarkTree_WhenUserNotFound_ReturnsNotFoundResponse() {
    String expectedHash = "expected-hash";
    Root root = Root.createNew(); // 🧠 Create once, use everywhere

    doThrow(new IllegalStateException("User not found"))
            .when(service)
            .updateTreeWithHashCheck(USER_ID, expectedHash, root);

    ResponseEntity<?> response = controller.putBookmarkTree(USER_ID, expectedHash, root); // ✅ use same root

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isEqualTo("Not Found: User not found");

    verify(service).updateTreeWithHashCheck(USER_ID, expectedHash, root); // ✅ same again
  }

}
