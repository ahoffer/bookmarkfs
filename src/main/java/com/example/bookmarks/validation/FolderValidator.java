package com.example.bookmarks.validation;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.bookmarks.model.Folder;
import com.example.bookmarks.model.TreeNode;
import java.util.UUID;

public class FolderValidator {
  private final ValidationService validator;

  public FolderValidator(ValidationService validationService) {
    this.validator = validationService;
  }

  private void assertValidUUID(String id, String label) {
    try {
      UUID uuid = UUID.fromString(id);
      assertThat(uuid.version()).as("%s must be a UUID v4", label).isEqualTo(4);
    } catch (IllegalArgumentException ex) {
      throw new AssertionError(label + " must be a valid UUID v4, but was: " + id, ex);
    }
  }

  public void validate(Folder folder) {
    assertValidUUID(folder.id(), "Folder.id");
    assertThat(folder.kind()).as("Folder.kind must be 'folder'").isEqualTo("folder");
    assertThat(folder.name()).as("Folder.name must not be null or blank").isNotBlank();
    assertThat(folder.contents()).as("Folder.contents must not be null").isNotNull();
    for (TreeNode child : folder.contents()) {
      validator.validate(child);
    }
  }
}
