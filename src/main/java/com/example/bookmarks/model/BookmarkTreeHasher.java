package com.example.bookmarks.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.MessageDigest;
import java.util.Base64;

public class BookmarkTreeHasher {
  private static final ObjectMapper mapper = new ObjectMapper();

  public static String hash(JsonNode json) {
    try {
      byte[] bytes = mapper.writeValueAsBytes(json);
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(bytes);
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    } catch (Exception e) {
      throw new RuntimeException("Failed to calculate SHA-256 hash of BookmarkTree", e);
    }
  }
}
