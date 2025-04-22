package com.example.bookmarks.model;

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

  /**
   * This method should only be used by the ObjectMapper and test classes
   *
   * @param contents
   */
  public Root {
    //    Jackson does NOT auto-convert null → empty list
    contents = contents == null ? List.of() : List.copyOf(contents);
  }

  public String hash() {
    DigestAccumulator acc = new DigestAccumulator();
    acc.updateAll(contents);
    return acc.digestBase64();
  }
}
