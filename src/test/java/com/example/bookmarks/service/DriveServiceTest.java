package com.example.bookmarks.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.bookmarks.model.RootFolder;
import com.example.bookmarks.model.RootFolderFactory;
import com.example.bookmarks.persistence.UserDrive;
import com.example.bookmarks.persistence.UserDriveRepository;
import com.example.bookmarks.validation.ValidationService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class DriveServiceTest {
  private UserDriveRepository repository;
  private DriveService service;
  private ValidationService validator;

  @Test
  void getUserDrive_found() {
    UserDrive drive = new UserDrive("user123", RootFolderFactory.createDefault());
    when(repository.findById("user123")).thenReturn(Optional.of(drive));
    Optional<UserDrive> result = service.getUserDrive("user123");
    assertThat(result).isPresent().containsSame(drive);
  }

  @Test
  void getUserDrive_notFound() {
    when(repository.findById("user123")).thenReturn(Optional.empty());
    Optional<UserDrive> result = service.getUserDrive("user123");
    assertThat(result).isEmpty();
  }

  @BeforeEach
  void setup() {
    repository = mock(UserDriveRepository.class);
    validator = mock(ValidationService.class);
    service = new DriveService(repository, validator);
    doNothing().when(validator).validate(any(RootFolder.class));
  }

  @Test
  void updateTreeWithHashCheck_hashMismatch() {
    String userId = "user123";
    String expectedHash = "abc123";
    RootFolder newRoot = mock(RootFolder.class);
    when(newRoot.hash()).thenReturn("newHashAfterUpdate");
    UserDrive current = new UserDrive(userId, newRoot, "wrongHash");
    when(repository.findById(userId)).thenReturn(Optional.of(current));
    assertThatThrownBy(() -> service.updateTreeWithHashCheck(userId, expectedHash, newRoot))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Hash mismatch. Expected: abc123, but was: wrongHash");
    verify(repository, never()).save(any());
  }

  @Test
  void updateTreeWithHashCheck_successful() {
    String userId = "user123";
    RootFolder newRoot = RootFolderFactory.createDefault();
    String expectedHash = newRoot.hash(); // current hash in DB

    UserDrive current = new UserDrive(userId, newRoot, expectedHash);
    when(repository.findById(userId)).thenReturn(Optional.of(current));
    service.updateTreeWithHashCheck(userId, expectedHash, newRoot);
    ArgumentCaptor<UserDrive> saved = ArgumentCaptor.forClass(UserDrive.class);
    verify(repository).save(saved.capture());
    UserDrive updated = saved.getValue();
    assertThat(updated.getData()).isEqualTo(newRoot);
    assertThat(updated.getCurrentHash())
        .as("Hash should be recalculated and match root.hash()")
        .isEqualTo(newRoot.hash());
  }

  @Test
  void updateTreeWithHashCheck_treeNotFound() {
    String userId = "user123";
    RootFolder root = mock(RootFolder.class);
    when(repository.findById(userId)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.updateTreeWithHashCheck(userId, "hash", root))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Tree not found");
    verify(repository, never()).save(any());
  }
}
