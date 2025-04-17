package com.example.bookmarks.validation;

import com.example.bookmarks.model.BookmarkTree;
import com.example.bookmarks.pipeline.Filter;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashSet;
import java.util.Set;

public class ValidateRequiredChildrenFilter implements Filter<BookmarkTree, BookmarkTree> {
  private static final Set<String> REQUIRED_ROOT_CHILD_IDS = Set.of("my-folder", "inbox", "trash");

  @Override
  public BookmarkTree apply(BookmarkTree tree) {
    JsonNode children = tree.getJsonNode().path("contents");
    if (!children.isArray()) {
      throw new IllegalArgumentException("Root must have a 'children' array");
    }

    Set<String> found = new HashSet<>();
    for (JsonNode child : children) {
      String id = child.path("id").asText();
      if (!id.isEmpty()) {
        found.add(id);
      }
    }

    if (!found.equals(REQUIRED_ROOT_CHILD_IDS)) {
      throw new IllegalArgumentException("Root must have exactly: my-folder, inbox, and trash");
    }

    return tree;
  }
}
