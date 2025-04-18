package com.example.bookmarks.model;

import java.util.List;
import java.util.UUID;

public final class RootFolderFactory {

  private RootFolderFactory() {}

  public static RootFolder createDefault() {
    Folder myFolder = new Folder(UUID.randomUUID().toString(), "folder", "my-folder", List.of());

    Folder inbox = new Folder(UUID.randomUUID().toString(), "folder", "inbox", List.of());

    Folder trash = new Folder(UUID.randomUUID().toString(), "folder", "trash", List.of());

    return new RootFolder("root", "root", "Root", List.of(myFolder, inbox, trash));
  }
}
