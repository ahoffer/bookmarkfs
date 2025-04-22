package com.example.bookmarks.persistence;

import com.example.bookmarks.model.Root;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RootFolderConverter implements AttributeConverter<Root, String> {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Root rootFolder) {
    try {
      return mapper.writeValueAsString(rootFolder);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to serialize RootFolder", e);
    }
  }

  @Override
  public Root convertToEntityAttribute(String dbData) {
    try {
      return mapper.readValue(dbData, Root.class);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to deserialize RootFolder", e);
    }
  }
}
