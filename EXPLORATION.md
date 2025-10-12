# Repository Exploration

This document captures a quick walkthrough of the project structure discovered during exploration:

- `pom.xml` – Maven configuration for building the Spring Boot project.
- `src/main/java/com/mde` – Application source code with configuration, controller, entity, service, and type definitions for the Todo sample application.
- `src/test/java/com/mde` – JUnit tests targeting the controller layer.
- `Readme.md` – Quick-start instructions for building and running the project.
- `todo.postman_collection.json` – Postman collection containing example HTTP requests for the API.

The service currently keeps todo items in memory using an `ArrayList`, exposing CRUD endpoints through `TodoController`.
