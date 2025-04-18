package com.example.bookmarks.persistence;

import com.example.bookmarks.model.RootFolder;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_drive")
public class UserDrive {

  @Id
  @Column(name = "user_id", nullable = false)
  private String userId;

  @Convert(converter = RootFolderConverter.class)
  @Column(name = "data", columnDefinition = "jsonb", nullable = false)
  private RootFolder data;

  @Column(name = "current_hash", nullable = false)
  private String currentHash;

  @Column(name = "updated_at", nullable = false)
  private Instant lastUpdated;

  public UserDrive() {}

  public UserDrive(String userId, RootFolder data) {
    this.userId = userId;
    this.data = data;
    this.lastUpdated = Instant.now();
  }

  public String getUserId() {
    return userId;
  }

  public RootFolder getData() {
    return data;
  }

  public void setData(RootFolder data) {
    this.data = data;
  }

  public String getCurrentHash() {
    return currentHash;
  }

  public void setCurrentHash(String currentHash) {
    this.currentHash = currentHash;
  }

  public Instant getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Instant lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}
