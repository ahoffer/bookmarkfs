package com.example.bookmarks.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "kind")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Folder.class, name = "folder"),
  @JsonSubTypes.Type(value = Bookmark.class, name = "bookmark")
})
public interface TreeNode {
  String getId();

  String getKind();

  String hash();
}
