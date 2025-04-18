package com.example.bookmarks.validation;

import static org.assertj.core.api.Assertions.*;

import com.example.bookmarks.model.Bookmark;
import com.example.bookmarks.model.Folder;
import com.example.bookmarks.model.RootFolder;
import com.example.bookmarks.model.TreeNode;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ModelValidatorTest {

  private ValidationService validator;

  @BeforeEach
  void setUp() {
    validator = new ValidationService();
  }

  @Test
  void validate_bookmark_doesNotThrow() {
    TreeNode node = new Bookmark(UUID.randomUUID().toString(), "bookmark", "https://example.com");

    assertThatCode(() -> validator.validate(node)).doesNotThrowAnyException();
  }

  @Test
  void validate_folder_doesNotThrow() {
    TreeNode node = new Folder(UUID.randomUUID().toString(), "folder", "My Folder", List.of());

    assertThatCode(() -> validator.validate(node)).doesNotThrowAnyException();
  }

  @Test
  void validate_rootFolder_doesNotThrow() {
    TreeNode node =
        new RootFolder(
            "root",
            "root",
            "root",
            List.of(
                new Folder(UUID.randomUUID().toString(), "folder", "my-folder", List.of()),
                new Folder(UUID.randomUUID().toString(), "folder", "inbox", List.of()),
                new Folder(UUID.randomUUID().toString(), "folder", "trash", List.of())));

    assertThatCode(() -> validator.validate(node)).doesNotThrowAnyException();
  }

  @Test
  void validate_nestedFolderStructure_doesNotThrow() {
    Bookmark bookmark =
        new Bookmark(UUID.randomUUID().toString(), "bookmark", "https://example.com");

    Folder nested = new Folder(UUID.randomUUID().toString(), "folder", "Nested", List.of(bookmark));

    RootFolder root =
        new RootFolder(
            "root",
            "root",
            "root",
            List.of(
                new Folder(UUID.randomUUID().toString(), "folder", "my-folder", List.of(nested)),
                new Folder(UUID.randomUUID().toString(), "folder", "inbox", List.of()),
                new Folder(UUID.randomUUID().toString(), "folder", "trash", List.of())));

    assertThatCode(() -> validator.validate(root)).doesNotThrowAnyException();
  }
}
