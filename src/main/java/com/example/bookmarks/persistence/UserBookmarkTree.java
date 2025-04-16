package com.example.bookmarks.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_bookmark_tree")
public class UserBookmarkTree {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "data", columnDefinition = "jsonb", nullable = false)
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode data;

    @Column(name = "current_hash", nullable = false)
    private String currentHash;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }

    public String getCurrentHash() {
        return currentHash;
    }

    public void setCurrentHash(String currentHash) {
        this.currentHash = currentHash;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
