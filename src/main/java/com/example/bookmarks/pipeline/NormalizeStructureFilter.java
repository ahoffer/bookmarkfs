package com.example.bookmarks.pipeline;

import com.example.bookmarks.model.BookmarkTree;

/** Use the object mapper to put all textual json into the same format */
public class NormalizeStructureFilter implements Filter<BookmarkTree, BookmarkTree> {
  private final Serializer serializer = new Serializer();
  private final Deserializer deserializer = new Deserializer();

  @Override
  public BookmarkTree apply(BookmarkTree tree) {
    return deserializer.apply(serializer.apply(tree));
  }
}
