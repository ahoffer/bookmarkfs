package com.example.bookmarks.service;

import com.example.bookmarks.model.BookmarkEntry;
import com.example.bookmarks.model.BookmarkTree;
import com.example.bookmarks.persistence.UserBookmarkTree;
import com.example.bookmarks.persistence.UserBookmarkTreeRepository;
import com.example.bookmarks.pipeline.*;
import com.example.bookmarks.validation.*;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkTreeService {

  private final UserBookmarkTreeRepository repository;

  public BookmarkTreeService(UserBookmarkTreeRepository repository) {
    this.repository = repository;
  }

  public Optional<UserBookmarkTree> getTree(String userId) {
    return repository.findById(userId);
  }

  @Transactional
  public void updateTreeWithHashCheck(String userId, String expectedHash, JsonNode newJsonTree) {
    UserBookmarkTree current =
        repository.findById(userId).orElseThrow(() -> new IllegalStateException("Tree not found"));

    Pipeline<BookmarkTree, BookmarkTree> pipeline =
        new Pipeline<>(
            List.of(
                new RejectIfHashMismatchFilter(current.getCurrentHash()),
                new NormalizeStructureFilter(),
                new ValidateRootFolderFilter(),
                new ValidateRequiredChildrenFilter(),
                new ValidateInboxAndTrashEmptyFilter()));

    BookmarkTree incomingTree = new BookmarkTree(newJsonTree);
    BookmarkTree validated = pipeline.run(incomingTree);

    current.setData(validated.getJsonNode());
    current.setCurrentHash(validated.hash());
    current.setUpdatedAt(Instant.now());
    repository.save(current);
  }

  @Transactional
  public void deliverToInbox(String userId, BookmarkEntry entry) {
    UserBookmarkTree current =
        repository.findById(userId).orElseThrow(() -> new IllegalStateException("Tree not found"));

    BookmarkTree tree = new BookmarkTree(current.getData()).deliverToInbox(entry);
    tree = new NormalizeStructureFilter().apply(tree);

    current.setData(tree.getJsonNode());
    current.setCurrentHash(tree.hash());
    current.setUpdatedAt(Instant.now());
    repository.save(current);
  }
}
