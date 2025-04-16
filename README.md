Initial file structure.

// Directory Structure

```
bookmark-tree/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/bookmarks/
│   │   │       ├── BookmarkTreeApplication.java
│   │   │       ├── api/
│   │   │       │   └── BookmarkTreeController.java
│   │   │       ├── model/
│   │   │       │   ├── BookmarkEntry.java
│   │   │       │   ├── BookmarkTree.java
│   │   │       │   └── BookmarkTreeHasher.java
│   │   │       ├── pipeline/
│   │   │       │   ├── Filter.java
│   │   │       │   ├── Pipeline.java
│   │   │       │   ├── NormalizeStructureFilter.java
│   │   │       │   ├── RejectIfHashMismatchFilter.java
│   │   │       │   ├── TreeDeserializer.java
│   │   │       │   └── TreeSerializer.java
│   │   │       ├── validation/
│   │   │       │   ├── ValidateRootFolderFilter.java
│   │   │       │   ├── ValidateRequiredChildrenFilter.java
│   │   │       │   └── ValidateInboxAndTrashEmptyFilter.java
│   │   │       └── service/
│   │   │           └── BookmarkTreeService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/example/bookmarks/model/
│               └── BookmarkTreeTest.java
```