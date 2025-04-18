package com.example.bookmarks.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

class DigestAccumulator {
  private final MessageDigest digest;

  public DigestAccumulator() {
    try {
      this.digest = MessageDigest.getInstance("MD5");
    } catch (Exception e) {
      throw new RuntimeException("Unable to initialize MD5 digest", e);
    }
  }

  public void update(String value) {
    if (value != null) {
      digest.update(value.getBytes(StandardCharsets.UTF_8));
    }
  }

  public void update(TreeNode node) {
    if (node != null) {
      update(node.hash());
    }
  }

  public void updateAll(TreeNode... nodes) {
    if (nodes != null) {
      for (TreeNode node : nodes) {
        update(node);
      }
    }
  }

  public void updateAll(Iterable<? extends TreeNode> nodes) {
    if (nodes != null) {
      for (TreeNode node : nodes) {
        update(node);
      }
    }
  }

  public String digestBase64() {
    byte[] hash = digest.digest();
    return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
  }
}
