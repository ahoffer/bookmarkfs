package com.example.bookmarks.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

public class BookmarkTree {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final JsonNode normalized;

    public BookmarkTree(JsonNode raw) {
        this.normalized = raw;
    }

    public JsonNode getNormalized() {
        return normalized;
    }

    public boolean isSemanticallyEqualTo(BookmarkTree other) {
        return this.normalized.equals(other.normalized);
    }

    public String hash() {
        return BookmarkTreeHasher.hash(normalized);
    }

    public BookmarkTree insertIntoInbox(BookmarkEntry entry) {
        ObjectNode root = (ObjectNode) normalized.deepCopy();
        ArrayNode children = (ArrayNode) root.withArray("children");

        for (JsonNode child : children) {
            if ("inbox".equals(child.path("id").asText()) && child.isObject()) {
                ObjectNode inbox = (ObjectNode) child;
                ArrayNode bookmarks = inbox.withArray("bookmarks");

                ObjectNode newBookmark = mapper.createObjectNode();
                newBookmark.put("id", entry.id());
                newBookmark.put("metaCardId", entry.metaCardId());
                newBookmark.put("title", entry.title());
                newBookmark.put("url", entry.url());

                bookmarks.add(newBookmark);
                return new BookmarkTree(root);
            }
        }

        throw new IllegalStateException("'inbox' folder not found under root");
    }
}
