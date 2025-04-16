package com.example.bookmarks.controller;

import com.example.bookmarks.model.BookmarkTree;
import com.example.bookmarks.service.BookmarkTreeService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/bookmark-tree")
public class BookmarkTreeController {

    private final BookmarkTreeService service;

    public BookmarkTreeController(BookmarkTreeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getBookmarkTree(@PathVariable String userId) {
        return service.getTree(userId)
                .map(tree -> ResponseEntity.ok()
                        .eTag('"' + tree.getCurrentHash() + '"')
                        .body(tree.getData()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<?> updateBookmarkTree(
            @PathVariable String userId,
            @RequestHeader("If-Match") String expectedHash,
            @RequestBody JsonNode newTreeJson
    ) {
        try {
            service.updateTreeWithHashCheck(userId, expectedHash, newTreeJson);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body("Conflict: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body("Not Found: " + e.getMessage());
        }
    }
}
