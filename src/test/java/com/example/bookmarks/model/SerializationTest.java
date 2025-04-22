package com.example.bookmarks.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

/**
 * Jackson uses reflection. JSON (de)serialiation) can cause states that are not possible with
 * plain-old coding.
 */
public class SerializationTest {

  @Test
  void folderWithNullContents_deserializedSafely() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json =
        """
        {
            "id": "550e8400-e29b-41d4-a716-446655440000",
            "type": "folder",
            "name": "test-folder"
        }
        """;

    Folder folder = mapper.readValue(json, Folder.class);

    assertThat(folder.contents()).isNotNull(); // ← this will fail without fix
    assertThat(folder.contents()).isEmpty();
  }
}
