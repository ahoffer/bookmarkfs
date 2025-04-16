package com.example.bookmarks.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(JsonNode attribute) {
    try {
      return mapper.writeValueAsString(attribute);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to convert JsonNode to String", e);
    }
  }

  @Override
  public JsonNode convertToEntityAttribute(String dbData) {
    try {
      return mapper.readTree(dbData);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to convert String to JsonNode", e);
    }
  }
}
