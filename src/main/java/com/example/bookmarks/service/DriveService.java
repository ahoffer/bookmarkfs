package com.example.bookmarks.service;

import com.example.bookmarks.model.RootFolder;
import com.example.bookmarks.persistence.UserDrive;
import com.example.bookmarks.persistence.UserDriveRepository;
import com.example.bookmarks.validation.ValidationService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriveService {
  private final UserDriveRepository repository;
  private final ValidationService validator;

  public DriveService(UserDriveRepository repository, ValidationService validator) {
    this.repository = repository;
    this.validator = validator;
  }

  public Optional<UserDrive> getUserDrive(String userId) {
    return repository.findById(userId);
  }

  @Transactional
  public void updateTreeWithHashCheck(String userId, String expectedHash, RootFolder rootFolder) {
    validator.validate(rootFolder);
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
