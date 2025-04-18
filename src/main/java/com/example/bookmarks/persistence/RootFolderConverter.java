package com.example.bookmarks.persistence;

import com.example.bookmarks.model.RootFolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RootFolderConverter implements AttributeConverter<RootFolder, String> {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(RootFolder rootFolder) {
    try {
      return mapper.writeValueAsString(rootFolder);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to serialize RootFolder", e);
    }
  }

  @Override
  public RootFolder convertToEntityAttribute(String dbData) {
    try {
      return mapper.readValue(dbData, RootFolder.class);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to deserialize RootFolder", e);
    }
  }
}
