package com.example.bookmarks.validation;

import com.example.bookmarks.model.BookmarkTree;
import com.example.bookmarks.pipeline.Filter;

public class ValidateRootFolderFilter implements Filter<BookmarkTree, BookmarkTree> {
  @Override
  public BookmarkTree apply(BookmarkTree tree) {
    if (!tree.getNormalized().path("id").asText().equals("root")) {
      throw new IllegalArgumentException("Top-level folder must have id = 'root'");
    }
    return tree;
  }
}
