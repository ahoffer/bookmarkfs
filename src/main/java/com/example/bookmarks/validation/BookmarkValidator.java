package com.example.bookmarks.validation;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.bookmarks.model.Bookmark;
import java.util.UUID;

public class BookmarkValidator {
  private void assertValidUUID(String id, String label) {
    try {
      UUID uuid = UUID.fromString(id);
      assertThat(uuid.version()).as("%s must be a UUID v4", label).isEqualTo(4);
    } catch (IllegalArgumentException ex) {
      throw new AssertionError(label + " must be a valid UUID v4, but was: " + id, ex);
    }
  }

  public void validate(Bookmark bookmark) {
    assertValidUUID(bookmark.id(), "Bookmark.id");
    assertThat(bookmark.kind()).as("Bookmark.kind must be 'bookmark'").isEqualTo("bookmark");
    assertThat(bookmark.url()).as("Bookmark.url must not be null or blank").isNotBlank();
  }
}
