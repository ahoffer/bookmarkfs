package com.example.bookmarks.validation;

import com.example.bookmarks.model.BookmarkTree;
import com.example.bookmarks.pipeline.Filter;
import com.fasterxml.jackson.databind.JsonNode;

public class ValidateInboxAndTrashEmptyFilter implements Filter<BookmarkTree, BookmarkTree> {
  @Override
  public BookmarkTree apply(BookmarkTree tree) {
    JsonNode children = tree.getNormalized().path("children");
    if (children.isArray()) {
      for (JsonNode child : children) {
        String id = child.path("id").asText();
        if (("inbox".equals(id) || "trash".equals(id)) && child.path("children").size() > 0) {
          throw new IllegalArgumentException("Folders under 'inbox' and 'trash' are not allowed");
        }
      }
    }
    return tree;
  }
}
