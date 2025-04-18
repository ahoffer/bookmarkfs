package com.example.bookmarks.service;

import com.example.bookmarks.model.RootFolder;
import com.example.bookmarks.persistence.UserDrive;
import com.example.bookmarks.persistence.UserDriveRepository;
import java.time.Instant;
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
  public void updateTreeWithHashCheck(String userId, String expectedHash, RootFolder rootFolder) {

    rootFolder.validateTopLevelStructure();
    UserDrive current =
        repository.findById(userId).orElseThrow(() -> new IllegalStateException("Tree not found"));
    if (!current.getCurrentHash().equals(expectedHash)) {
      throw new IllegalStateException(
          "Hash mismatch. Expected: " + expectedHash + ", but was: " + current.getCurrentHash());
    }
    current.setData(rootFolder);
    current.setCurrentHash(rootFolder.hash());
    current.setLastUpdated(Instant.now());
    repository.save(current);
  }
}
