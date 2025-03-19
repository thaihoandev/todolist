package com.interview.todolist.controllers;

import com.interview.todolist.dtos.PagedResponse;
import com.interview.todolist.models.Task;
import com.interview.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Create a new task
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // Get all tasks
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<Task>> getAllTasks(
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Task> tasks = taskService.getAllTasks(filterBy, filterValue, page, size);
        PagedResponse<Task> response = new PagedResponse<>(tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update task by ID
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK); // Return OK status
    }

    // Delete task by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Add a dependency to a task
    @PostMapping("/{taskId}/dependencies/{dependencyId}")
    public ResponseEntity<?> addDependency(@PathVariable Long taskId, @PathVariable Long dependencyId) {
        try {
            Task updatedTask = taskService.addDependency(taskId, dependencyId);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Remove a dependency from a task
    @DeleteMapping("/{taskId}/dependencies/{dependencyId}")
    public ResponseEntity<Void> removeDependency(@PathVariable Long taskId, @PathVariable Long dependencyId) {
        taskService.removeDependency(taskId, dependencyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get dependencies of a task
    @GetMapping(value = "/{taskId}/dependencies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Task>> getTaskDependencies(@PathVariable Long taskId) {
        try {
            List<Task> dependencies = taskService.getTaskDependencies(taskId);
            return new ResponseEntity<>(dependencies, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
