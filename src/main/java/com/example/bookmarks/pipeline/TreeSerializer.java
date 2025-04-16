package com.example.bookmarks.pipeline;

import com.example.bookmarks.model.BookmarkTree;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TreeSerializer implements Filter<BookmarkTree, String> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String apply(BookmarkTree tree) {
        try {
            return mapper.writeValueAsString(tree.getNormalized());
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize BookmarkTree", e);
        }
    }
}
