package com.example.bookmarks.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.bookmarks.model.Root;
import com.example.bookmarks.persistence.UserDrive;
import com.example.bookmarks.persistence.UserDriveRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class DriveServiceTest {
  private UserDriveRepository repository;
  private DriveService service;

  @Test
  void getUserDrive_found() {
    UserDrive drive = new UserDrive("user123", Root.createNew());
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
    service = new DriveService(repository);
  }

  @Test
  void putDriveWithHashCheck_Mismatch() {
    String userId = "user123";
    String expectedHash = "abc123";
    Root newRoot = mock(Root.class);
    when(newRoot.hash()).thenReturn("newHashAfterUpdate");
    UserDrive current = new UserDrive(userId, newRoot, "wrongHash");
    when(repository.findById(userId)).thenReturn(Optional.of(current));
    assertThatThrownBy(() -> service.putDriveWithFreshnessCheck(userId, expectedHash, newRoot))
        .isInstanceOf(ServiceExceptions.HashMismatchException.class)
        .hasMessageContaining("abc123").hasMessageContaining("wrongHash");
    verify(repository, never()).save(any());
  }

  @Test
  void putDriveWithFreshnessCheck_successful() {
    String userId = "user123";
    Root newRoot = Root.createNew();
    String expectedHash = newRoot.hash(); // current hash in DB

    UserDrive current = new UserDrive(userId, newRoot, expectedHash);
    when(repository.findById(userId)).thenReturn(Optional.of(current));
    service.putDriveWithFreshnessCheck(userId, expectedHash, newRoot);
    ArgumentCaptor<UserDrive> saved = ArgumentCaptor.forClass(UserDrive.class);
    verify(repository).save(saved.capture());
    UserDrive updated = saved.getValue();
    assertThat(updated.getData()).isEqualTo(newRoot);
    assertThat(updated.getCurrentHash())
        .as("Hash should be recalculated and match root.hash()")
        .isEqualTo(newRoot.hash());
  }

  @Test
  void putDriveNotFound() {
    String userId = "user123";
    Root root = mock(Root.class);
    when(repository.findById(userId)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.putDriveWithFreshnessCheck(userId, "hash", root))
        .isInstanceOf(ServiceExceptions.UserNotFoundException.class);
    verify(repository, never()).save(any());
  }
}
