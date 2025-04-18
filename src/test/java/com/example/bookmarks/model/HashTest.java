package com.example.bookmarks.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public class HashTest {

  @Test
  void bookmarkHashChangesWithUrl() {
    Bookmark b1 = new Bookmark("id1", "bookmark", "https://example.com");
    Bookmark b2 = new Bookmark("id1", "bookmark", "https://different.com");

    assertThat(b1.hash()).isNotEqualTo(b2.hash());
  }

  @Test
  void folderHashIncludesContents() {
    Bookmark b1 = new Bookmark("id1", "bookmark", "https://a.com");
    Bookmark b2 = new Bookmark("id2", "bookmark", "https://b.com");

    Folder f1 = new Folder("fid", "folder", "Folder", List.of(b1));
    Folder f2 = new Folder("fid", "folder", "Folder", List.of(b2));

    assertThat(f1.hash()).isNotEqualTo(f2.hash());
  }

  @Test
  void rootFolderHashIsStableForSameStructure() {
    Bookmark b1 = new Bookmark("id1", "bookmark", "https://a.com");
    Folder folder1 = new Folder("fid1", "folder", "my-folder", List.of(b1));
    RootFolder root1 = new RootFolder("root", "folder", "Root", List.of(folder1));

    Bookmark b2 = new Bookmark("id1", "bookmark", "https://a.com");
    Folder folder2 = new Folder("fid1", "folder", "my-folder", List.of(b2));
    RootFolder root2 = new RootFolder("root", "folder", "Root", List.of(folder2));

    assertThat(root1.hash()).isEqualTo(root2.hash());
  }

  @Test
  void rootFolderHashChangesWithContentChange() {
    Bookmark b1 = new Bookmark("id1", "bookmark", "https://a.com");
    Bookmark b2 = new Bookmark("id2", "bookmark", "https://a.com");

    Folder folder1 = new Folder("fid1", "folder", "my-folder", List.of(b1));
    Folder folder2 = new Folder("fid1", "folder", "my-folder", List.of(b2));

    RootFolder root1 = new RootFolder("root", "folder", "Root", List.of(folder1));
    RootFolder root2 = new RootFolder("root", "folder", "Root", List.of(folder2));

    assertThat(root1.hash()).isNotEqualTo(root2.hash());
  }
}
