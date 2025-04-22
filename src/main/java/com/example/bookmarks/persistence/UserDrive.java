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
  private String hash;

  @Column(name = "updated", nullable = false)
  private Instant lastUpdated;

  protected UserDrive() {
    // For JPA
  }

  public UserDrive(String userId, Root data) {
    this.userId = userId;
    setData(data); // ensures hash is updated
  }

  // This is for JSON serialization. It is not meant to be used in plain-old Java code.
  public UserDrive(String userId, Root data, String hash) {
    this.userId = userId;
    this.data = data;
    this.hash = hash;
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
    this.hash = data != null ? data.hash() : null;
  }

  public String getHash() {
    return hash;
  }
}
