package com.example.bookmarks.validation;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.bookmarks.model.Folder;
import com.example.bookmarks.model.RootFolder;
import com.example.bookmarks.model.TreeNode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RootFolderValidator {
  private static final Set<String> REQUIRED_NAMES = Set.of("my-folder", "inbox", "trash");
  private final ModelValidator validator;

  public RootFolderValidator(ModelValidator validator) {
    this.validator = validator;
  }

  public void validate(RootFolder root) {
    assertThat(root.id()).as("RootFolder.id must be 'root'").isEqualTo("root");
    assertThat(root.kind()).as("RootFolder.kind must be 'root'").isEqualTo("root");
    assertThat(root.name()).as("RootFolder.name must be 'root'").isEqualTo("root");
    List<TreeNode> contents = root.getContents();
    assertThat(contents).as("RootFolder.contents must contain exactly 3 items").hasSize(3);
    List<Folder> foldersOnly =
        contents.stream().filter(Folder.class::isInstance).map(Folder.class::cast).toList();
    assertThat(foldersOnly).as("All RootFolder children must be folders").hasSize(3);
    Set<String> names = foldersOnly.stream().map(Folder::name).collect(Collectors.toSet());
    assertThat(names)
        .as("Top-level folders must be: %s", REQUIRED_NAMES)
        .containsExactlyInAnyOrderElementsOf(REQUIRED_NAMES);
    for (Folder folder : foldersOnly) {
      validator.validate(folder); // recurse
    }
  }
}
