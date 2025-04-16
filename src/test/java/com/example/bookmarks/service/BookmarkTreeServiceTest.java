package com.example.bookmarks.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.bookmarks.model.BookmarkEntry;
import com.example.bookmarks.persistence.UserBookmarkTree;
import com.example.bookmarks.persistence.UserBookmarkTreeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class BookmarkTreeServiceTest {

  private UserBookmarkTreeRepository repository;
  private BookmarkTreeService service;

  @BeforeEach
  void setup() {
    repository = mock(UserBookmarkTreeRepository.class);
    service = new BookmarkTreeService(repository);
  }

  @Test
  void getTree_returnsExistingTree() {
    var tree = new UserBookmarkTree();
    when(repository.findById("user123")).thenReturn(Optional.of(tree));

    var result = service.getTree("user123");

    assertThat(result).isPresent().contains(tree);
  }

  @Test
  void updateTreeWithHashCheck_replacesDataAndSaves() throws Exception {
    var oldTree = new UserBookmarkTree();
    oldTree.setCurrentHash("expectedHash");
    oldTree.setData(
        new ObjectMapper()
            .readTree(
                """
            { "root": { "children": [], "kind": "root" } }
        """));

    when(repository.findById("user123")).thenReturn(Optional.of(oldTree));

    JsonNode newJsonTree =
        new ObjectMapper()
            .readTree(
                """
            {
              "root": {
                "children": [
                  { "id": "bookmark-1", "kind": "bookmark", "name": "Test", "url": "https://example.com" }
                ],
                "kind": "root"
              }
            }
        """);

    service.updateTreeWithHashCheck("user123", "expectedHash", newJsonTree);

    verify(repository).save(any(UserBookmarkTree.class));
    assertThat(oldTree.getCurrentHash()).isNotNull();
    assertThat(oldTree.getData().toString()).contains("bookmark-1");
  }

  @Test
  void insertIntoInbox_appendsBookmarkAndSaves() throws Exception {
    JsonNode currentData =
        new ObjectMapper()
            .readTree(
                """
            {
              "root": {
                "children": [
                  { "id": "inbox", "kind": "folder", "children": [] },
                  { "id": "trash", "kind": "folder", "children": [] },
                  { "id": "my-folder", "kind": "folder", "children": [] }
                ],
                "kind": "root"
              }
            }
        """);

    var oldTree = new UserBookmarkTree();
    oldTree.setData(currentData);
    oldTree.setCurrentHash("initial");
    when(repository.findById("user123")).thenReturn(Optional.of(oldTree));

    var entry = new BookmarkEntry("new-entry", "bookmark", "Inbox Item", "https://chat.openai.com");

    service.insertIntoInbox("user123", entry);

    ArgumentCaptor<UserBookmarkTree> captor = ArgumentCaptor.forClass(UserBookmarkTree.class);
    verify(repository).save(captor.capture());

    var updated = captor.getValue();
    assertThat(updated.getData().toString()).contains("Inbox Item");
  }

  @Test
  void updateTreeWithHashCheck_throwsIfTreeMissing() {
    when(repository.findById("missingUser")).thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> service.updateTreeWithHashCheck("missingUser", "any", mock(JsonNode.class)))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Tree not found");
  }
}
