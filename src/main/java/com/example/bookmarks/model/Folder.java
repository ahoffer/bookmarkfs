package com.example.bookmarks.model;

import java.util.List;

public record Folder(String id, String kind, String name, List<TreeNode> contents)
    implements TreeNode {

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
}
