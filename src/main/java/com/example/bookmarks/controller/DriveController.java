package com.example.bookmarks.controller;

import com.example.bookmarks.model.RootFolder;
import com.example.bookmarks.service.DriveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/bookmark-tree")
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
  public ResponseEntity<?> updateBookmarkTree(
      @PathVariable String userId,
      @RequestHeader("If-Match") String expectedHash,
      @RequestBody RootFolder newTree) {
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
