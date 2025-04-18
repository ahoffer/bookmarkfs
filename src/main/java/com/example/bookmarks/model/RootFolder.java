package com.example.bookmarks.model;

import java.util.List;

public record RootFolder(String id, String kind, String name, List<TreeNode> contents)
    implements TreeNode {

  public RootFolder {
    if (!"root".equals(id)) {
      throw new IllegalArgumentException("Root folder must have id 'root'");
    }
    if (!"folder".equals(kind)) {
      throw new IllegalArgumentException("Root must have kind 'folder'");
    }
    if (!"Root".equals(name)) {
      throw new IllegalArgumentException("Root folder must have name 'Root'");
    }
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getKind() {
    return kind;
  }

  @Override
  public String hash() {
    DigestAccumulator acc = new DigestAccumulator();
    acc.update(id);
    acc.update(kind);
    acc.update(name);
    acc.updateAll(contents);
    return acc.digestBase64();
  }

  public List<TreeNode> getContents() {
    return contents;
  }

  public void validateTopLevelStructure() {
    List<String> allowed = List.of("my-folder", "inbox", "trash");

    for (TreeNode child : contents) {
      if (!(child instanceof Folder folder)) {
        throw new IllegalArgumentException("Only folders are allowed directly under root");
      }
      if (!allowed.contains(folder.name())) {
        throw new IllegalArgumentException("Invalid top-level folder: " + folder.name());
      }
    }
  }
}
