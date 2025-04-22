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
  public UserDrive putDriveWithFreshnessCheck(String userId, String expectedHash, Root newRoot) {
    UserDrive current =
        repository
            .findById(userId)
            .orElseThrow(
                () ->
                    new ServiceExceptions.UserNotFoundException(
                        "No drive found for user: " + userId));

    String actualHash = current.getCurrentHash();
    if (!actualHash.equals(expectedHash)) {
      throw new ServiceExceptions.HashMismatchException(
          "Expected: " + expectedHash + ", but was: " + actualHash);
    }

    current.setData(newRoot);
    return repository.save(current);
  }

  @Transactional
  public UserDrive createUser(String userId) {
    if (repository.existsById(userId)) {
      throw new IllegalArgumentException("User already exists");
    }
    Root root = Root.createNew();
    UserDrive entity = new UserDrive(userId, root);
    return repository.save(entity);
  }
}
