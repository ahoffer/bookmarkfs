package com.example.bookmarks.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Folder.class, name = "folder"),
  @JsonSubTypes.Type(value = Bookmark.class, name = "bookmark")
})
public sealed interface TreeNode permits Bookmark, Folder, RootFolder {
  String getId();

  String getType();

  String hash();
}
