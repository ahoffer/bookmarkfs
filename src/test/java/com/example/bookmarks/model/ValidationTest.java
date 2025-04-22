package com.example.bookmarks.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ValidationTest {

  Validation validation = new Validation();

  private static String randomId() {
    return UUID.randomUUID().toString();
  }

  @Test
  void validBookmark_passesValidation() {
    Bookmark b = new Bookmark(randomId(), "bookmark", "example", "https://example.com");
    Validation.ValidationContext ctx = validation.validate(b);
    assertThat(ctx.getErrors()).isEmpty();
  }

  @Test
  void invalidBookmark_missingFields_reportsErrors() {
    Bookmark b = new Bookmark(null, "wrong", null, "not-a-url");
    Validation.ValidationContext ctx = validation.validate(b);

    assertThat(ctx.getErrors()).hasSizeGreaterThan(1);
    assertThat(ctx.getErrors())
        .anySatisfy(e -> assertThat(e.toString()).contains("id"))
        .anySatisfy(e -> assertThat(e.toString()).contains("name"))
        .anySatisfy(e -> assertThat(e.toString()).contains("type"))
        .anySatisfy(e -> assertThat(e.toString()).contains("url"));
  }

  @Test
  void validFolder_withValidBookmark_passesValidation() {
    Bookmark child = new Bookmark(randomId(), "bookmark", "name", "https://valid.com");
    Folder f = new Folder(randomId(), "folder", "sub-folder", List.of(child));
    Validation.ValidationContext ctx = validation.validate(f);

    assertThat(ctx.getErrors()).isEmpty();
  }

  @Test
  void invalidFolder_reportsNestedErrors() {
    Bookmark broken = new Bookmark("", "bookmark", "", "ftp://missing-host");
    Folder f = new Folder("", "folder", "", List.of(broken));
    Validation.ValidationContext ctx = validation.validate(f);

    assertThat(ctx.getErrors()).isNotEmpty();
    assertThat(ctx.getErrors())
        .anySatisfy(e -> assertThat(e.path()).contains("children"))
        .anySatisfy(e -> assertThat(e.message()).contains("Missing or blank"));
  }
}
