package com.example.bookmarks.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;

public final class Root {
  private final List<Folder> contents;

  public static Root createNew() {
    return new Root(
        List.of(
            new Folder(UUID.randomUUID().toString(), "folder", "my-folder", List.of()),
            new Folder(UUID.randomUUID().toString(), "folder", "inbox", List.of()),
            new Folder(UUID.randomUUID().toString(), "folder", "trash", List.of())));
  }

  /** Contructor for JSON mapper
   *
   * @param contents
   */
  protected Root(List<Folder> contents) {
    this.contents = List.copyOf(contents);
  }

  @JsonCreator
  public static Root fromJson(@JsonProperty("contents") List<Folder> contents) {
    return new Root(contents);
  }

  public List<Folder> contents() {
    return contents;
  }

  public String hash() {
    DigestAccumulator acc = new DigestAccumulator();
    contents.forEach(acc::update);
    return acc.digestBase64();
  }
}
