package com.example.bookmarks.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Validation {

  static final List<String> REQUIRED_NAMES = List.of("my-folder", "inbox", "trash");

  final ValidationContext ctx = new ValidationContext();

  public ValidationContext validate(TreeNode node) {
    return validateNode(node);
  }

  public ValidationContext validate(Root root) {
    return validateRoot(root);
  }

  ValidationContext validateBookmark(Bookmark b) {
    validateId(b.id());
    validateName(b.name());

    ctx.runWithPath(
        "type",
        () -> {
          if (!"bookmark".equals(b.type())) {
            ctx.addError("Expected type to be 'bookmark' but was: '" + b.type() + "'");
          }
        });

    validateUrl(b.url());
    return ctx;
  }

  ValidationContext validateFolder(Folder f) {
    validateId(f.id());
    validateName(f.name());

    ctx.runWithPath(
        "type",
        () -> {
          if (!"folder".equals(f.type())) {
            ctx.addError("Expected type to be 'folder' but was: '" + f.type() + "'");
          }
        });

    ctx.runWithPath(
        "children",
        () -> {
          for (int i = 0; i < f.contents().size(); i++) {
            final int index = i;
            ctx.runWithPath("[" + index + "]", () -> validateNode(f.contents().get(index)));
          }
        });
    return ctx;
  }

  ValidationContext validateRoot(Root root) {
    ctx.runWithPath(
        "contents",
        () -> {
          List<? extends TreeNode> contents = root.contents();

          if (contents.size() != 3) {
            ctx.addError("Root must contain exactly 3 folders: my-folder, inbox, and trash");
          }

          for (int i = 0; i < contents.size(); i++) {
            TreeNode child = contents.get(i);
            ctx.runWithPath(
                "[" + i + "]",
                () -> {
                  if (!(child instanceof Folder f)) {
                    ctx.addError(
                        "All children of root must be folders. Found: "
                            + child.getClass().getSimpleName());
                  } else {
                    String name = f.name();
                    if (!REQUIRED_NAMES.contains(name)) {
                      ctx.addError(
                          "Unexpected folder name: '"
                              + name
                              + "'. Must be one of: "
                              + REQUIRED_NAMES);
                    }

                    validateFolder(f); // run the rest of folder validation
                  }
                });
          }

          // Check for exact match of names
          List<String> actualNames = contents.stream().map(n -> ((Folder) n).name()).toList();

          if (!actualNames.containsAll(REQUIRED_NAMES)
              || !REQUIRED_NAMES.containsAll(actualNames)) {
            ctx.addError(
                "Root must contain exactly these folders: "
                    + REQUIRED_NAMES
                    + ". Found: "
                    + actualNames);
          }
        });

    return ctx;
  }

  ValidationContext validateNode(TreeNode node) {
    switch (node) {
      case Bookmark b -> validateBookmark(b);
      case Folder f -> validateFolder(f);
    }
    return ctx;
  }

  ValidationContext validateId(String id) {
    ctx.runWithPath(
        "id",
        () -> {
          if (id == null || id.isBlank()) {
            ctx.addError("Missing or blank ID");
          }
        });
    return ctx;
  }

  ValidationContext validateName(String name) {
    ctx.runWithPath(
        "name",
        () -> {
          if (name == null || name.isBlank()) {
            ctx.addError("Missing or blank name");
          }
        });
    return ctx;
  }

  ValidationContext validateUrl(String uri) {
    ctx.runWithPath(
        "url",
        () -> {
          if (uri == null || uri.isBlank()) {
            ctx.addError("Bookmark URL is missing or blank");
          } else {
            try {
              URI uriObject = new URI(uri);
              if (!uriObject.isAbsolute() || uriObject.getHost() == null) {
                ctx.addError("Bookmark URL must be an absolute URI with a host: '" + uri + "'");
              }
            } catch (URISyntaxException e) {
              ctx.addError("Bookmark URL is malformed: '" + uri + "' — " + e.getMessage());
            }
          }
        });
    return ctx;
  }

  public static class ValidationContext {
    final List<ValidationError> errors = new ArrayList<>();
    final StringBuilder path = new StringBuilder();

    public void addError(String message) {
      errors.add(new ValidationError(path.toString(), message));
    }

    public List<ValidationError> getErrors() {
      return errors;
    }

    public boolean hasErrors() {
      return !errors.isEmpty();
    }

    public void pushPath(String segment) {
      if (path.length() > 0) path.append(".");
      path.append(segment);
    }

    public void popPath() {
      int lastDot = path.lastIndexOf(".");
      if (lastDot == -1) {
        path.setLength(0);
      } else {
        path.setLength(lastDot);
      }
    }

    public void runWithPath(String segment, Runnable action) {
      pushPath(segment);
      try {
        action.run();
      } finally {
        popPath();
      }
    }
  }

  public static record ValidationError(String path, String message) {
    @Override
    public String toString() {
      return "Error at '" + path + "': " + message;
    }
  }

  public static class ValidationException extends RuntimeException {
    private final List<ValidationError> errors;

    public ValidationException(List<ValidationError> errors) {
      super(buildMessage(errors));
      this.errors = List.copyOf(errors);
    }

    public List<ValidationError> getErrors() {
      return errors;
    }

    static String buildMessage(List<ValidationError> errors) {
      if (errors == null || errors.isEmpty()) {
        return "Validation failed with no details.";
      }
      return "Validation failed:\n"
          + errors.stream()
              .map(e -> e.path() + ": " + e.message())
              .reduce((a, b) -> a + "\n" + b)
              .orElse("Unknown validation error.");
    }
  }
}
