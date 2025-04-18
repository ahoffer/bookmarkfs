package com.example.bookmarks.model;

public record Bookmark(String id, String kind, String url) implements TreeNode {
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
    acc.update(url);
    return acc.digestBase64();
  }
}
