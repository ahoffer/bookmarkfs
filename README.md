Bookmark File System

Overview
- Folder hierarchy with folders containing both bookmarks and other folders.
- UserDrive is the top-level object; it contains a user ID and the user's folder tree.
- Retrieve a user's drive with a GET /{userId}. Returns 404 Not Found if the user does not exist.
- All folder tree manipulation is done client-side.
- Update the entire drive with a PUT /{userId} — replaces existing data.
- Simple conflict detection via hashes; conflicting PUTs are rejected.
- Structure validation is performed before accepting an update.
- User IDs are chosen by the client (e.g., a username). Uniqueness is enforced: "Sorry, that name is already taken..."
- Create a new user with a POST /{userId}. Returns 409 Conflict if the user already exists.
- Tree has a fixed top-level structure: home, inbox, and trash folders.
- A special Root object anchors the tree — it cannot itself hold content.

Features
- Simple design — no need for GraphQL.
- API documentation via annotations.
- Spring Boot RestController and CrudController.
- Postgres DB. One table.
- Integration tests live in a separate repository.
- Dockerized (compose files extracted and run as part of itests).
- Built with the latest version of Spring Boot.
- Solid, focused domain models.

TODO
- Rename "my folder" to "home"?
- Remove application.properties — switch to environment variables.
- Add authentication.
- Move integration tests into this repo and convert them to use MockMVC.
