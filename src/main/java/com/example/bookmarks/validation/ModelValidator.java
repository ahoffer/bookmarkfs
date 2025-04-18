package com.example.bookmarks.validation;

import com.example.bookmarks.model.Bookmark;
import com.example.bookmarks.model.Folder;
import com.example.bookmarks.model.RootFolder;
import com.example.bookmarks.model.TreeNode;
import org.springframework.stereotype.Component;

@Component
public class ModelValidator {

  private final BookmarkValidator bookmarkValidator = new BookmarkValidator();
  private final FolderValidator folderValidator = new FolderValidator(this);
  private final RootFolderValidator rootFolderValidator = new RootFolderValidator(this);

  public void validate(TreeNode node) {
    switch (node) {
      case Bookmark bookmark -> bookmarkValidator.validate(bookmark);
      case Folder folder -> folderValidator.validate(folder);
      case RootFolder root -> rootFolderValidator.validate(root);
    }
  }
}
