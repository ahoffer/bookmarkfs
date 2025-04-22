package com.example.bookmarks.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RootValidationTest {

  Validation validation = new Validation();

  private static Folder folder(String name) {
    return new Folder(UUID.randomUUID().toString(), "folder", name, List.of());
  }

  @Test
  void rootWithBlankName_failsValidation() {
    Folder blankName = new Folder(UUID.randomUUID().toString(), "folder", "", List.of());
    Root root = Root.fromJson(List.of(blankName, folder("inbox"), folder("trash")));

    Validation.ValidationContext ctx = validation.validate(root);

    assertThat(ctx.hasErrors()).isTrue();
    assertThat(ctx.getErrors())
        .anySatisfy(e -> assertThat(e.path()).contains("name"))
        .anySatisfy(e -> assertThat(e.message()).contains("Missing or blank name"));
  }

  @Test
  void rootWithDuplicateFolderNames_failsValidation() {
    Folder inbox1 = folder("inbox");
    Folder inbox2 = folder("inbox");
    Folder myFolder = folder("my-folder");

    Root root = Root.fromJson(List.of(inbox1, inbox2, myFolder));

    Validation.ValidationContext ctx = validation.validate(root);

    assertThat(ctx.hasErrors()).isTrue();
    assertThat(ctx.getErrors())
        .anySatisfy(e -> assertThat(e.message()).contains("must contain exactly these folders"));
  }

  @Test
  void rootWithMislabeledFolderType_failsValidation() {
    Folder wrongType =
        new Folder(UUID.randomUUID().toString(), "not-a-folder", "my-folder", List.of());
    Root root = Root.fromJson(List.of(wrongType, folder("inbox"), folder("trash")));

    Validation.ValidationContext ctx = validation.validate(root);

    assertThat(ctx.hasErrors()).isTrue();
    assertThat(ctx.getErrors())
        .anySatisfy(e -> assertThat(e.message()).contains("Expected type to be 'folder'"));
  }

  @Test
  void rootWithTooFewFolders_failsValidation() {
    Root root = Root.fromJson(List.of(folder("my-folder"), folder("inbox"))); // only two

    Validation.ValidationContext ctx = validation.validate(root);

    assertThat(ctx.hasErrors()).isTrue();
    assertThat(ctx.getErrors())
        .anySatisfy(e -> assertThat(e.message()).contains("exactly 3 folders"))
        .anySatisfy(e -> assertThat(e.message()).contains("must contain exactly these folders"));
  }

  @Test
  void rootWithUnexpectedFolderName_failsValidation() {
    Folder bad = folder("downloads"); // invalid name
    Root root = Root.fromJson(List.of(bad, folder("inbox"), folder("trash")));

    Validation.ValidationContext ctx = validation.validate(root);

    assertThat(ctx.hasErrors()).isTrue();
    assertThat(ctx.getErrors())
        .anySatisfy(e -> assertThat(e.message()).contains("Unexpected folder name"))
        .anySatisfy(e -> assertThat(e.message()).contains("must contain exactly these folders"));
  }

  @Test
  void rootWithValidFolders_passesValidation() {
    Root root = Root.createNew();

    Validation.ValidationContext ctx = validation.validate(root);

    assertThat(ctx.hasErrors()).isFalse();
  }
}
