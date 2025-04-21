package com.example.bookmarks.validation;

import static org.assertj.core.api.Assertions.*;

import com.example.bookmarks.model.Bookmark;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookmarkValidatorTest {

  private BookmarkValidator validator;

  @BeforeEach
  void setUp() {
    validator = new BookmarkValidator();
  }

  @Test
  void validate_validBookmark_doesNotThrow() {
    Bookmark valid = new Bookmark(UUID.randomUUID().toString(), "bookmark", "https://example.com");

    assertThatCode(() -> validator.validate(valid)).doesNotThrowAnyException();
  }

  @Test
  void validate_invalidUUID_throwsAssertionError() {
    Bookmark invalid = new Bookmark("not-a-uuid", "bookmark", "https://example.com");

    assertThatThrownBy(() -> validator.validate(invalid))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Bookmark.id must be a valid UUID v4");
  }

  @Test
  void validate_wrongUUIDVersion_throwsAssertionError() {
    // UUID v1
    Bookmark invalid =
        new Bookmark(
            UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8").toString(),
            "bookmark",
            "https://example.com");

    assertThatThrownBy(() -> validator.validate(invalid))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Bookmark.id must be a UUID v4");
  }

  @Test
  void validate_wrongKind_throwsAssertionError() {
    Bookmark invalid =
        new Bookmark(UUID.randomUUID().toString(), "not-bookmark", "https://example.com");

    assertThatThrownBy(() -> validator.validate(invalid))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Bookmark.type must be 'bookmark'");
  }

  @Test
  void validate_blankUrl_throwsAssertionError() {
    Bookmark invalid = new Bookmark(UUID.randomUUID().toString(), "bookmark", "   ");

    assertThatThrownBy(() -> validator.validate(invalid))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Bookmark.url must not be null or blank");
  }
}
