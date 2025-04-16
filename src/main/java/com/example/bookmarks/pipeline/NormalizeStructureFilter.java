package com.example.bookmarks.pipeline;

import com.example.bookmarks.model.BookmarkTree;

public class NormalizeStructureFilter implements Filter<BookmarkTree, BookmarkTree> {
    private final TreeSerializer serializer = new TreeSerializer();
    private final TreeDeserializer deserializer = new TreeDeserializer();

    @Override
    public BookmarkTree apply(BookmarkTree tree) {
        return deserializer.apply(serializer.apply(tree));
    }
}
