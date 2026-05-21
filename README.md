# Task Manager

A full-stack task management application with a Java Spring Boot backend and a Vue 3 frontend.

## Features

- Create tasks with title, optional description, status, and due date/time
- Retrieve a task by ID
- Retrieve all tasks
- Update a full task
- Update only task status
- Delete tasks
- Database persistence with H2 for local development
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
- H2 Database
- JUnit 5
- Mockito
- MockMvc

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

## Database

The application uses H2 for local development and evaluation.

No external database installation is required.

To use PostgreSQL instead, update the datasource configuration in:

```txt
backend/src/main/resources/application.properties
```

## Running the Backend

```bash
cd backend
mvn spring-boot:run
```

The backend runs at:

```txt
http://localhost:8080
```

The H2 console is available at:

```txt
http://localhost:8080/h2-console
```

Use the following connection details:

```txt
JDBC URL: jdbc:h2:mem:tasksdb
Username: sa
Password:
```

Leave the password blank.

The default CORS origin is:

```txt
http://localhost:5173
```

## Running the Frontend

```bash
cd frontend
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

## Building the Frontend

```bash
cd frontend
npm run build
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
- `status` is optional on create and defaults to `TODO`.
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

If the task does not exist:

```txt
404 Not Found
```

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

Response:

```txt
200 OK
```

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

Response:

```txt
200 OK
```

Returns the updated task.

### Delete Task

```http
DELETE /tasks/{id}
```

Response:

```txt
204 No Content
```

If the task does not exist:

```txt
404 Not Found
```

## Error Response Format

### Validation Error Example

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

### Not Found Example

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

No environment variables are required for the default H2 configuration.

Optional:

```txt
APP_CORS_ALLOWED_ORIGINS=http://localhost:5173
```

### Frontend

```txt
VITE_API_BASE_URL=http://localhost:8080/api
```

## Project Structure

```txt
task-manager/
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── application.properties
├── frontend/
│   ├── src/
│   ├── tests/
│   ├── package.json
│   └── vite.config.ts
└── README.md
```

## Notes

- The application uses an in-memory H2 database for local development and evaluation.
- Data is reset whenever the backend application restarts.
- PostgreSQL can be enabled by updating the datasource configuration in `backend/src/main/resources/application.properties`.
- Backend tests run against H2.
- Frontend tests use Vitest and Vue Testing Library.