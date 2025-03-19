# To-Do List Application

This is a Spring Boot application for managing a To-Do List with features like task creation, dependency management, notifications for upcoming/overdue tasks, caching for performance, and a background scheduler for periodic tasks.

## Features

- **Task Management**: Create, read, update, and delete tasks.
- **Task Dependencies**: Add and remove dependencies between tasks (a task can only depend on a completed task).
- **Task Filtering**: Filter tasks by priority, completion status, due date, or title.
- **Pagination**: Pagination support for listing tasks.
- **Notifications**: Notification system for upcoming and overdue tasks.
- **Background Scheduler**: Checks for upcoming/overdue tasks daily at 8:00 AM.

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java 21 or later** (the project uses Java 22 in the logs, but Java 17+ should work).
- **Maven** (for dependency management and building the project).
- **H2 Database** (in-memory or file-based).
- A **code editor** like IntelliJ IDEA or Visual Studio Code.

## Setup Instructions

### 1. Clone the Repository

Clone the project to your local machine:

```bash
git clone https://github.com/thaihoandev/todolist.git
cd todolist
```
### 2. Build the project using Maven

### 3. Run project


## Testing

You can test the APIs using tools like **Postman** or **cURL**. Example workflow:

1. **Create a task**:

```bash
POST /api/tasks
{
  "title": "Test Task",
  "dueDate": "2025-03-20",
  "priority": "High",
  "completed": false
}
```
2. **Get all tasks**:

```bash
POST /api/tasks?page=1&size=10&filterBy=priority&filterValue=High
```
3. **Get all tasks**:

```bash
POST /api/tasks/{id}
```


4. **Update a task**:

```bash
PUT /api/tasks
{
  "title": "Test Task",
  "dueDate": "2025-03-20",
  "priority": "High",
  "completed": true
}
```
5. **Delele a task**:

```bash
DELETE /api/tasks
```
6. **Add a Dependency**:

```bash
POST /api/tasks/{taskId}/dependencies/{dependencyId}
```
7. **Get Task Dependencies**:

```bash
GET /api/tasks/{taskId}/dependencies
```
8. **Remove a Dependency**:

```bash
DELETE /api/tasks/{taskId}/dependencies/{dependencyId}
```