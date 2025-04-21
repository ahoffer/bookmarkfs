package com.example.bookmarks.model;

import java.util.List;

public record RootFolder(String id, String type, String name, List<TreeNode> contents)
    implements TreeNode {

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getType() {
    return type;
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

  public List<TreeNode> getContents() {
    return contents;
  }
}
