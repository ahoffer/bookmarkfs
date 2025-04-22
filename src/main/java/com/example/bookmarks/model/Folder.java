package com.example.bookmarks.model;

import java.util.List;

public record Folder(String id, String type, String name, List<TreeNode> contents)
    implements TreeNode {

  public Folder {
    contents = contents == null ? List.of() : List.copyOf(contents);
  }

  @Override
  public String hash() {
    DigestAccumulator acc = new DigestAccumulator();
    acc.update(id);
    acc.update(type);
    acc.update(name);
    acc.updateAll(contents);
    return acc.digestBase64();
  }
}
