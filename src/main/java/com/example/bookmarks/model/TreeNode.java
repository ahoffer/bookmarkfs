package com.example.bookmarks.model;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Folder.class, name = "folder"),
  @JsonSubTypes.Type(value = Bookmark.class, name = "bookmark"),
})
public sealed interface TreeNode permits Bookmark, Folder {

  String hash();

  String id();

  String name();

  String type();
}
