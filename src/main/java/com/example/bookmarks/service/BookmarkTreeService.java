package com.example.bookmarks.service;

import com.example.bookmarks.model.BookmarkTree;
import com.example.bookmarks.model.BookmarkEntry;
import com.example.bookmarks.pipeline.*;
import com.example.bookmarks.validation.*;
import com.example.bookmarks.persistence.UserBookmarkTree;
import com.example.bookmarks.persistence.UserBookmarkTreeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.List;

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
        UserBookmarkTree current = repository.findById(userId)
            .orElseThrow(() -> new IllegalStateException("Tree not found"));

        Pipeline<BookmarkTree, BookmarkTree> pipeline = new Pipeline<>(List.of(
            new RejectIfHashMismatchFilter(current.getCurrentHash()),
            new NormalizeStructureFilter(),
            new ValidateRootFolderFilter(),
            new ValidateRequiredChildrenFilter(),
            new ValidateInboxAndTrashEmptyFilter()
        ));

        BookmarkTree incomingTree = new BookmarkTree(newJsonTree);
        BookmarkTree validated = pipeline.run(incomingTree);

        current.setData(validated.getNormalized());
        current.setCurrentHash(validated.hash());
        current.setUpdatedAt(Instant.now());
        repository.save(current);
    }

    @Transactional
    public void insertIntoInbox(String userId, BookmarkEntry entry) {
        UserBookmarkTree current = repository.findById(userId)
            .orElseThrow(() -> new IllegalStateException("Tree not found"));

        BookmarkTree tree = new BookmarkTree(current.getData()).insertIntoInbox(entry);
        tree = new NormalizeStructureFilter().apply(tree);

        current.setData(tree.getNormalized());
        current.setCurrentHash(tree.hash());
        current.setUpdatedAt(Instant.now());
        repository.save(current);
    }
}
