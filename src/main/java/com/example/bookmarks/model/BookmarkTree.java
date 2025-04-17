package com.example.bookmarks.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.util.Iterator;
import java.util.Map;

public class BookmarkTree {
  private static final ObjectMapper mapper = new ObjectMapper();
  private final JsonNode jsonNode;

  public BookmarkTree(JsonNode raw) {
    this.jsonNode = raw;
  }

  public JsonNode getJsonNode() {
    return jsonNode;
  }

  // TODO If we need this, run it through a pipeline to normalize it.
  //  public boolean isSemanticallyEqualTo(BookmarkTree other) {
  //    return this.jsonNode.equals(other.jsonNode);
  //  }

  public String hash() {
    return BookmarkTreeHasher.hash(jsonNode);
  }

  public BookmarkTree deliverToInbox(BookmarkEntry entry) {
    ObjectNode node = jsonNode.deepCopy();
    JsonNode rootContents = node.path("root").path("contents");
    rootContents.elements().forEachRemaining(o -> {
      ArrayNode x = (ArrayNode) o.get("contents");
      ArrayNode y = x.add(mapper.valueToTree(entry));


    });
//    if (inboxNode.isMissingNode()) {
//      throw new IllegalStateException("'inbox' folder not found under root");
//    }
//        ArrayNode inboxContents = inboxNode.withArray("contents");
//        ObjectNode newItem = mapper.valueToTree(entry);
//        inboxContents.add(newItem);
        return new BookmarkTree(node);
      }
    }

