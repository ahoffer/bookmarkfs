package com.example.bookmarks.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

/**
 * Jackson uses reflection. JSON (de)serialiation) can cause states that are not possible with
 * plain-old coding.
 */
public class SerializationTest {

  public static final ObjectMapper MAPPER = new ObjectMapper();

  @Test
  void folderWithNullContents_deserializedSafely() throws Exception {

    String json =
        """
        {
            "id": "550e8400-e29b-41d4-a716-446655440000",
            "type": "folder",
            "name": "test-folder"
        }
        """;

    Folder folder = MAPPER.readValue(json, Folder.class);

    assertThat(folder.contents()).isNotNull(); // ← this will fail without fix
    assertThat(folder.contents()).isEmpty();
  }


  @Test
  void serializeRoot() throws JsonProcessingException {
    Root expected = Root.createNew();
    Root actual = MAPPER.readValue(MAPPER.writeValueAsString(expected), Root.class);
    assertThat(actual).isEqualTo(expected);
  }
}
