package com.example.bookmarks.model;

public record Bookmark(String id, String type, String name, String url) implements TreeNode {

  @Override
  public String hash() {
    DigestAccumulator acc = new DigestAccumulator();
    acc.update(id);
    acc.update(type());
    acc.update(url);
    return acc.digestBase64();
  }
}
