# Task Manager

A full-stack task management application with a Java Spring Boot backend and a Vue 3 frontend.

## Features

- Create tasks with title, optional description, status, and due date/time
- Retrieve a task by ID
- Retrieve all tasks
- Update a full task
- Update only task status
- Delete tasks
- Database persistence with PostgreSQL
- Backend validation and global error handling
- Frontend loading, empty, error, create, edit, status update, and delete states
- Backend and frontend tests

## Tech Stack

### Backend

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Jakarta Bean Validation
- PostgreSQL
- H2 for integration tests
- JUnit 5, Mockito, MockMvc

### Frontend

- Vue 3
- Vite
- TypeScript
- Vitest
- Vue Testing Library

## Prerequisites

- Java 21+
- Maven 3.9+
- Node.js 20+
- npm
- Docker, for local PostgreSQL

## Running the Database

From the project root:

```bash
docker compose up -d
```

This starts PostgreSQL on port `5432` with:

```txt
Database: tasksdb
Username: tasksuser
Password: taskspassword
```

## Running the Backend

```bash
cd backend
cp .env.example .env
mvn spring-boot:run
```

The backend runs at:

```txt
http://localhost:8080
```

The default CORS origin is:

```txt
http://localhost:5173
```

## Running the Frontend

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```

The frontend runs at:

```txt
http://localhost:5173
```

## Running Tests

### Backend

```bash
cd backend
mvn test
```

### Frontend

```bash
cd frontend
npm install
npm test
```

## API Documentation

Base URL:

```txt
http://localhost:8080/api
```

### Task Status Values

```txt
TODO
IN_PROGRESS
DONE
```

### Create Task

```http
POST /tasks
Content-Type: application/json
```

Request body:

```json
{
  "title": "Submit assignment",
  "description": "Finish backend and frontend implementation",
  "status": "TODO",
  "dueDate": "2026-05-25T17:00:00Z"
}
```

Notes:

- `title` is required and must not be blank.
- `description` is optional.
- `status` is optional on create. If omitted, it defaults to `TODO`.
- `dueDate` is required and must be a valid ISO datetime.

Response: `201 Created`

```json
{
  "id": "8d5e8e3a-75ac-4b17-a20f-887d94cc990f",
  "title": "Submit assignment",
  "description": "Finish backend and frontend implementation",
  "status": "TODO",
  "dueDate": "2026-05-25T17:00:00Z",
  "createdAt": "2026-05-21T10:00:00Z",
  "updatedAt": "2026-05-21T10:00:00Z"
}
```

### Get All Tasks

```http
GET /tasks
```

Response: `200 OK`

```json
[
  {
    "id": "8d5e8e3a-75ac-4b17-a20f-887d94cc990f",
    "title": "Submit assignment",
    "description": "Finish backend and frontend implementation",
    "status": "TODO",
    "dueDate": "2026-05-25T17:00:00Z",
    "createdAt": "2026-05-21T10:00:00Z",
    "updatedAt": "2026-05-21T10:00:00Z"
  }
]
```

### Get Task by ID

```http
GET /tasks/{id}
```

Response: `200 OK`

Returns a single task.

If the task does not exist, response is `404 Not Found`.

### Update Task

```http
PUT /tasks/{id}
Content-Type: application/json
```

Request body:

```json
{
  "title": "Submit assignment updated",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "dueDate": "2026-05-26T17:00:00Z"
}
```

Response: `200 OK`

Returns the updated task.

### Update Task Status

```http
PATCH /tasks/{id}/status
Content-Type: application/json
```

Request body:

```json
{
  "status": "DONE"
}
```

Response: `200 OK`

Returns the updated task.

### Delete Task

```http
DELETE /tasks/{id}
```

Response: `204 No Content`

If the task does not exist, response is `404 Not Found`.

## Error Response Format

Validation error example:

```json
{
  "timestamp": "2026-05-21T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "title": "Title is required"
  }
}
```

Not found example:

```json
{
  "timestamp": "2026-05-21T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Task not found with id: 8d5e8e3a-75ac-4b17-a20f-887d94cc990f"
}
```

## Environment Variables

### Backend

```txt
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/tasksdb
SPRING_DATASOURCE_USERNAME=tasksuser
SPRING_DATASOURCE_PASSWORD=taskspassword
APP_CORS_ALLOWED_ORIGINS=http://localhost:5173
```

### Frontend

```txt
VITE_API_BASE_URL=http://localhost:8080/api
```

## Notes
- Backend tests use H2 in PostgreSQL compatibility mode.
