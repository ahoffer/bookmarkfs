package com.example.bookmarks.validation;

import static org.assertj.core.api.Assertions.*;

import com.example.bookmarks.model.Bookmark;
import com.example.bookmarks.model.Folder;
import com.example.bookmarks.model.TreeNode;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FolderValidatorTest {
  private FolderValidator folderValidator;

  @BeforeEach
  void setUp() {
    // Use the actual recursive validator
    ValidationService validationService = new ValidationService();
    folderValidator = new FolderValidator(validationService);
  }

  @Test
  void validate_folderWithBlankName_throwsAssertionError() {
    Folder folder = new Folder(UUID.randomUUID().toString(), "folder", "   ", List.of());
    assertThatThrownBy(() -> folderValidator.validate(folder))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Folder.name must not be null or blank");
  }

  @Test
  void validate_folderWithInvalidBookmark_throwsAssertionError() {
    TreeNode child = new Bookmark("not-a-uuid", "bookmark", "https://example.com");
    Folder folder = new Folder(UUID.randomUUID().toString(), "folder", "My Folder", List.of(child));
    assertThatThrownBy(() -> folderValidator.validate(folder))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Bookmark.id must be a valid UUID v4");
  }

  @Test
  void validate_folderWithInvalidUuid_throwsAssertionError() {
    Folder folder = new Folder("not-a-uuid", "folder", "My Folder", List.of());
    assertThatThrownBy(() -> folderValidator.validate(folder))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Folder.id must be a valid UUID v4");
  }

  @Test
  void validate_folderWithNullContents_throwsAssertionError() {
    Folder folder = new Folder(UUID.randomUUID().toString(), "folder", "My Folder", null);
    assertThatThrownBy(() -> folderValidator.validate(folder))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Folder.contents must not be null");
  }

  @Test
  void validate_folderWithWrongKind_throwsAssertionError() {
    Folder folder = new Folder(UUID.randomUUID().toString(), "not-folder", "My Folder", List.of());
    assertThatThrownBy(() -> folderValidator.validate(folder))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Folder.type must be 'folder'");
  }

  @Test
  void validate_validFolderWithValidBookmark_doesNotThrow() {
    TreeNode child = new Bookmark(UUID.randomUUID().toString(), "bookmark", "https://example.com");
    Folder folder = new Folder(UUID.randomUUID().toString(), "folder", "My Folder", List.of(child));
    assertThatCode(() -> folderValidator.validate(folder)).doesNotThrowAnyException();
  }
}
