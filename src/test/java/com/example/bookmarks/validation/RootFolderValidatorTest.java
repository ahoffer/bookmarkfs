package com.example.bookmarks.validation;

import static org.assertj.core.api.Assertions.*;

import com.example.bookmarks.model.Folder;
import com.example.bookmarks.model.RootFolder;
import com.example.bookmarks.model.TreeNode;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RootFolderValidatorTest {
  private RootFolderValidator rootValidator;

  private Folder folder(String name) {
    return new Folder(UUID.randomUUID().toString(), "folder", name, List.of());
  }

  @BeforeEach
  void setUp() {
    rootValidator = new RootFolderValidator(new ValidationService());
  }

  @Test
  void validate_missingTopLevelFolder_throwsAssertionError() {
    RootFolder root =
        new RootFolder(
            "root", "root", "root", List.of(folder("my-folder"), folder("inbox")) // only 2 folders
            );
    assertThatThrownBy(() -> rootValidator.validate(root))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("RootFolder.contents must contain exactly 3 items");
  }

  @Test
  void validate_nonFolderChild_throwsAssertionError() {
    TreeNode nonFolder =
        new com.example.bookmarks.model.Bookmark(
            UUID.randomUUID().toString(), "bookmark", "https://example.com");
    RootFolder root =
        new RootFolder(
            "root", "root", "root", List.of(folder("my-folder"), folder("inbox"), nonFolder));
    assertThatThrownBy(() -> rootValidator.validate(root))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("All RootFolder children must be folders");
  }

  @Test
  void validate_validRootFolder_doesNotThrow() {
    RootFolder root =
        new RootFolder(
            "root", "root", "root", List.of(folder("my-folder"), folder("inbox"), folder("trash")));
    assertThatCode(() -> rootValidator.validate(root)).doesNotThrowAnyException();
  }

  @Test
  void validate_wrongId_throwsAssertionError() {
    RootFolder root =
        new RootFolder(
            "not-root",
            "root",
            "root",
            List.of(folder("my-folder"), folder("inbox"), folder("trash")));
    assertThatThrownBy(() -> rootValidator.validate(root))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("RootFolder.id must be 'root'");
  }

  @Test
  void validate_wrongKind_throwsAssertionError() {
    RootFolder root =
        new RootFolder(
            "root",
            "not-root",
            "root",
            List.of(folder("my-folder"), folder("inbox"), folder("trash")));
    assertThatThrownBy(() -> rootValidator.validate(root))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("RootFolder.type must be 'root'");
  }

  @Test
  void validate_wrongName_throwsAssertionError() {
    RootFolder root =
        new RootFolder(
            "root",
            "root",
            "not-root",
            List.of(folder("my-folder"), folder("inbox"), folder("trash")));
    assertThatThrownBy(() -> rootValidator.validate(root))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("RootFolder.name must be 'root'");
  }

  @Test
  void validate_wrongTopLevelNames_throwsAssertionError() {
    RootFolder root =
        new RootFolder(
            "root", "root", "root", List.of(folder("downloads"), folder("inbox"), folder("trash")));
    assertThatThrownBy(() -> rootValidator.validate(root))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Top-level folders must be");
  }
}
