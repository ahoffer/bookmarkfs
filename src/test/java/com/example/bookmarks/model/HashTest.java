package com.example.bookmarks.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public class HashTest {

  @Test
  void identicalBookmarksProduceSameHash() {
    Bookmark b1 = new Bookmark("id1", "bookmark", "title", "https://example.com");
    Bookmark b2 = new Bookmark("id1", "bookmark", "title", "https://example.com");
    assertThat(b1.hash()).isEqualTo(b2.hash());
  }

  @Test
  void changingBookmarkIdChangesHash() {
    Bookmark b1 = new Bookmark("id1", "bookmark", "title", "https://example.com");
    Bookmark b2 = new Bookmark("id2", "bookmark", "title", "https://example.com");
    assertThat(b1.hash()).isNotEqualTo(b2.hash());
  }

  @Test
  void changingBookmarkUrlChangesHash() {
    Bookmark b1 = new Bookmark("id1", "bookmark", "title", "https://example.com");
    Bookmark b2 = new Bookmark("id1", "bookmark", "title", "https://different.com");
    assertThat(b1.hash()).isNotEqualTo(b2.hash());
  }

  @Test
  void identicalFoldersProduceSameHash() {
    Bookmark b1 = new Bookmark("id1", "bookmark", "a", "https://a.com");
    Bookmark b2 = new Bookmark("id2", "bookmark", "b", "https://b.com");
    Folder f1 = new Folder("fid", "folder", "my-folder", List.of(b1, b2));
    Folder f2 = new Folder("fid", "folder", "my-folder", List.of(b1, b2));
    assertThat(f1.hash()).isEqualTo(f2.hash());
  }

  @Test
  void folderHashChangesWithContentOrder() {
    Bookmark b1 = new Bookmark("id1", "bookmark", "a", "https://a.com");
    Bookmark b2 = new Bookmark("id2", "bookmark", "b", "https://b.com");
    Folder f1 = new Folder("fid", "folder", "my-folder", List.of(b1, b2));
    Folder f2 = new Folder("fid", "folder", "my-folder", List.of(b2, b1));
    assertThat(f1.hash()).isNotEqualTo(f2.hash());
  }

  @Test
  void folderHashChangesWithDifferentName() {
    Bookmark b = new Bookmark("id1", "bookmark", "a", "https://a.com");
    Folder f1 = new Folder("fid", "folder", "my-folder", List.of(b));
    Folder f2 = new Folder("fid", "folder", "different-name", List.of(b));
    assertThat(f1.hash()).isNotEqualTo(f2.hash());
  }

  @Test
  void rootFolderHashIsStableForSameStructure() {
    Bookmark b = new Bookmark("id1", "bookmark", "a", "https://a.com");
    Folder myFolder1 = new Folder("f1", "folder", "my-folder", List.of(b));
    Folder inbox1 = new Folder("f2", "folder", "inbox", List.of());
    Folder trash1 = new Folder("f3", "folder", "trash", List.of());

    Root root1 = new Root(List.of(myFolder1, inbox1, trash1));

    Bookmark b2 = new Bookmark("id1", "bookmark", "a", "https://a.com");
    Folder myFolder2 = new Folder("f1", "folder", "my-folder", List.of(b2));
    Folder inbox2 = new Folder("f2", "folder", "inbox", List.of());
    Folder trash2 = new Folder("f3", "folder", "trash", List.of());

    Root root2 = new Root(List.of(myFolder2, inbox2, trash2));

    assertThat(root1.hash()).isEqualTo(root2.hash());
  }

  @Test
  void rootFolderHashChangesWithAnyContentChange() {
    Bookmark b1 = new Bookmark("id1", "bookmark", "a", "https://a.com");
    Bookmark b2 = new Bookmark("id2", "bookmark", "a", "https://a.com"); // different ID
    Folder folder1 = new Folder("f1", "folder", "my-folder", List.of(b1));
    Folder inbox = new Folder("f2", "folder", "inbox", List.of());
    Folder trash = new Folder("f3", "folder", "trash", List.of());

    Folder folder2 = new Folder("f1", "folder", "my-folder", List.of(b2));

    Root root1 = new Root(List.of(folder1, inbox, trash));
    Root root2 = new Root(List.of(folder2, inbox, trash));

    assertThat(root1.hash()).isNotEqualTo(root2.hash());
  }

  @Test
  void rootFolderHashChangesWithFolderOrderChange() {
    Folder a = new Folder("id1", "folder", "my-folder", List.of());
    Folder b = new Folder("id2", "folder", "inbox", List.of());
    Folder c = new Folder("id3", "folder", "trash", List.of());

    Root originalOrder = new Root(List.of(a, b, c));
    Root swappedOrder = new Root(List.of(c, b, a));

    assertThat(originalOrder.hash()).isNotEqualTo(swappedOrder.hash());
  }
}
