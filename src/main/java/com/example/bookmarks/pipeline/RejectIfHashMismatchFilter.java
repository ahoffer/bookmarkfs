package com.example.bookmarks.pipeline;

import com.example.bookmarks.model.BookmarkTree;

public class RejectIfHashMismatchFilter implements Filter<BookmarkTree, BookmarkTree> {
  private final String expectedHash;

  public RejectIfHashMismatchFilter(String expectedHash) {
    this.expectedHash = expectedHash;
  }

  @Override
  public BookmarkTree apply(BookmarkTree tree) {
    if (!tree.hash().equals(expectedHash)) {
      throw new IllegalArgumentException(
          "Hash mismatch: bookmark tree has changed or is out of sync");
    }
    return tree;
  }
}
