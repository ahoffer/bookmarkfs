package com.example.bookmarks.factory;

import com.example.bookmarks.model.Folder;
import com.example.bookmarks.model.RootFolder;
import java.util.List;
import java.util.UUID;

public class RootFolderFactory {
  public static RootFolder createDefault() {
    return new RootFolder(
        "root",
        "folder",
        "Root",
        List.of(
            new Folder(UUID.randomUUID().toString(), "folder", "my-folder", List.of()),
            new Folder(UUID.randomUUID().toString(), "folder", "inbox", List.of()),
            new Folder(UUID.randomUUID().toString(), "folder", "trash", List.of())));
  }
}
