package com.example.bookmarks.service;

import com.example.bookmarks.model.Root;
import com.example.bookmarks.persistence.UserDrive;
import com.example.bookmarks.persistence.UserDriveRepository;
import com.example.bookmarks.service.ServiceExceptions.UserAlreadyExistsException;
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
  public UserDrive putDriveWithFreshnessCheck(UserDrive clientEntity) {
    UserDrive serverEntity =
        repository
            .findById(clientEntity.getUserId())
            .orElseThrow(
                () ->
                    new ServiceExceptions.UserNotFoundException(
                        "No user found: " + clientEntity.getUserId()));

    String expectedHash = serverEntity.getHash();
    String providedHash = clientEntity.getHash();
    if (!providedHash.equals(expectedHash)) {
      throw new ServiceExceptions.HashMismatchException(
          "Expected: " + expectedHash + ", but was: " + providedHash);
    }

    serverEntity.setData(clientEntity.getData());
    return repository.save(serverEntity);
  }

  @Transactional
  public UserDrive createUser(String userId) {
    if (repository.existsById(userId)) {
      throw new UserAlreadyExistsException("User " + userId + "already exists");
    }
    Root root = Root.createNew();
    UserDrive entity = new UserDrive(userId, root);
    return repository.save(entity);
  }
}
