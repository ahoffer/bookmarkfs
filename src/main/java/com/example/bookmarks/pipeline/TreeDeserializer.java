package com.example.bookmarks.pipeline;

import com.example.bookmarks.model.BookmarkTree;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TreeDeserializer implements Filter<String, BookmarkTree> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public BookmarkTree apply(String input) {
        try {
            JsonNode json = mapper.readTree(input);
            return new BookmarkTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize BookmarkTree from JSON", e);
        }
    }
}
