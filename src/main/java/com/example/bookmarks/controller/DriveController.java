package com.example.bookmarks.controller;

import com.example.bookmarks.model.Root;
import com.example.bookmarks.model.Validation;
import com.example.bookmarks.model.Validation.ValidationContext;
import com.example.bookmarks.service.DriveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/vfs")
public class DriveController {

  private final DriveService service;

  public DriveController(DriveService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<?> getBookmarkTree(@PathVariable String userId) {
    return service
        .getUserDrive(userId)
        .map(
            tree ->
                ResponseEntity.ok().eTag('"' + tree.getCurrentHash() + '"').body(tree.getData()))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping
  public ResponseEntity<?> putBookmarkTree(
      @PathVariable String userId,
      @RequestHeader("If-Match") String expectedHash,
      @RequestBody Root newTree) {

    ValidationContext result = new Validation().validate(newTree);
    if (result.hasErrors()) {
      throw new Validation.ValidationException(result.getErrors());
    }

    try {
      service.updateTreeWithHashCheck(userId, expectedHash, newTree);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(409).body("Conflict: " + e.getMessage());
    } catch (IllegalStateException e) {
      return ResponseEntity.status(404).body("Not Found: " + e.getMessage());
    }
  }
}
