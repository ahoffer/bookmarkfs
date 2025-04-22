package com.example.bookmarks.service;

import com.example.bookmarks.model.Root;
import com.example.bookmarks.persistence.UserDrive;
import com.example.bookmarks.persistence.UserDriveRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriveService {
  private final UserDriveRepository repository;

  public DriveService(UserDriveRepository repository) {
    this.repository = repository;
  }

  public Optional<UserDrive> getUserDrive(String userId) {
    return repository.findById(userId);
  }

  @Transactional
  public void updateTreeWithHashCheck(String userId, String expectedHash, Root rootFolder) {
    UserDrive current =
        repository.findById(userId).orElseThrow(() -> new IllegalStateException("Tree not found"));
    if (!current.getCurrentHash().equals(expectedHash)) {
      throw new IllegalStateException(
          "Hash mismatch. Expected: " + expectedHash + ", but was: " + current.getCurrentHash());
    }
    current.setData(rootFolder); // this will recalculate hash automatically
    repository.save(current);
  }
}
