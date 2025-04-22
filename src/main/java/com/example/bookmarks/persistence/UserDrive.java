package com.example.bookmarks.persistence;

import com.example.bookmarks.model.Root;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_drive")
public class UserDrive {

  @Id
  @Column(name = "user_id", nullable = false, updatable = false)
  private String userId;

  @Convert(converter = RootFolderConverter.class)
  @Column(name = "data", columnDefinition = "text", nullable = false)
  private Root data;

  @Column(name = "current_hash", nullable = false)
  private String currentHash;

  @Column(name = "updated", nullable = false)
  private Instant lastUpdated;

  protected UserDrive() {
    // For JPA
  }

  public UserDrive(String userId, Root data) {
    this.userId = userId;
    setData(data); // ensures hash is updated
  }

  public UserDrive(String userId, Root data, String currentHash) {
    this.userId = userId;
    this.data = data;
    this.currentHash = currentHash;
  }

  // Update the timestamp when saved to the DB
  @PrePersist
  @PreUpdate
  private void updateTimestamp() {
    this.lastUpdated = Instant.now();
  }

  public String getUserId() {
    return userId;
  }

  public Root getData() {
    return data;
  }

  public void setData(Root data) {
    this.data = data;
    this.currentHash = data != null ? data.hash() : null;
  }

  public String getCurrentHash() {
    return currentHash;
  }

  public void setCurrentHashManually(String currentHash) {
    this.currentHash = currentHash;
  }

  public Instant getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Instant lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}
