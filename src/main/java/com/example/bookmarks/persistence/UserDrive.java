package com.example.bookmarks.persistence;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.AUTO;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.bookmarks.model.Root;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "drives")
public class UserDrive {

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  @Schema(name = "Virtual Drive", requiredMode = AUTO, description = "The user's virtual drive")
  private Root data;

  @Column(name = "hash", nullable = false)
  @Schema(
      name = "Current Hash",
      example = "abc123hash",
      requiredMode = AUTO,
      description =
          "Hash at the time the object was retrieved from the server. If the hash on the service has not changed, the update is allowed to proceed.")
  private String hash;

  @Column(name = "updated", nullable = false)
  @Schema(
      name = "Last Updated",
      example = "2025-04-22T18:00:00Z",
      requiredMode = NOT_REQUIRED,
      description = "Server timestamp of last update")
  private Instant lastUpdated;

  @Id
  @Column(name = "user_id", nullable = false, updatable = false)
  @Schema(
      name = "User ID",
      example = "93091aa9-9e01-40eb-88bc-6f465156f3c7",
      requiredMode = REQUIRED,
      description = "UUIDv4 identifier for the user")
  private String userId;

  protected UserDrive() {
    // For JPA
  }

  public UserDrive(String userId, Root data) {
    this.userId = userId;
    setData(data); // ensures hash is updated
  }

  public UserDrive(String userId, Root data, String hash) {
    this.userId = userId;
    this.data = data;
    this.hash = hash;
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

  public Instant getLastUpdated() {
    return lastUpdated;
  }

  public String getUserId() {
    return userId;
  }

  @PrePersist
  @PreUpdate
  private void updateTimestamp() {
    this.lastUpdated = Instant.now();
  }
}
