package com.example.bookmarks.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;

public record Root(List<Folder> contents) {

  public static Root createNew() {
    return new Root(
            List.of(
                    new Folder(UUID.randomUUID().toString(), "folder", "my-folder", List.of()),
                    new Folder(UUID.randomUUID().toString(), "folder", "inbox", List.of()),
                    new Folder(UUID.randomUUID().toString(), "folder", "trash", List.of())));
  }

  public Root {
    contents = List.copyOf(contents); // Defensive copy
  }

  @JsonCreator
  public static Root fromJson(@JsonProperty("contents") List<Folder> contents) {
    return new Root(contents);
  }

  public String hash() {
    DigestAccumulator acc = new DigestAccumulator();
    contents.forEach(acc::update);
    return acc.digestBase64();
  }
}