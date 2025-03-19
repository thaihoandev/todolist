package com.interview.todolist.services;

import com.interview.todolist.dtos.TaskDTO;
import com.interview.todolist.models.Task;
import com.interview.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public TaskDTO createTask(Task task) {
        Task savedTask = taskRepository.save(task);
        return new TaskDTO(savedTask.getId(), savedTask.getTitle(), savedTask.getDescription(),
                savedTask.getDueDate(), savedTask.getPriority(), savedTask.isCompleted());
    }

    public Page<TaskDTO> getAllTasks(String filterBy, String filterValue, int page, int size) {
        int adjustedPage = page - 1;
        if (adjustedPage < 0) {
            adjustedPage = 0;
        }

        Pageable pageable = PageRequest.of(adjustedPage, size);
        if (filterBy != null && filterValue != null) {
            switch (filterBy.toLowerCase()) {
                case "priority":
                    return taskRepository.findByPriority(filterValue, pageable);
                case "completed":
                    return taskRepository.findByCompleted(Boolean.parseBoolean(filterValue), pageable);
                case "duedate":
                    return taskRepository.findByDueDate(LocalDate.parse(filterValue), pageable);
                case "title":
                    return taskRepository.findByTitleContaining(filterValue, pageable);
                default:
                    return taskRepository.findAllDTO(pageable);
            }
        }
        return taskRepository.findAllDTO(pageable);
    }

    public Optional<TaskDTO> getTaskById(Long taskId) {
        return taskRepository.findDTOById(taskId);
    }

    @Transactional
    public TaskDTO updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setDueDate(taskDetails.getDueDate());
        task.setPriority(taskDetails.getPriority());
        task.setCompleted(taskDetails.isCompleted());
        Task updatedTask = taskRepository.save(task);
        return new TaskDTO(updatedTask.getId(), updatedTask.getTitle(), updatedTask.getDescription(),
                updatedTask.getDueDate(), updatedTask.getPriority(), updatedTask.isCompleted());
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public TaskDTO addDependency(Long taskId, Long dependencyId) {
        Task task = taskRepository.findByIdWithDependencies(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Task dependency = taskRepository.findByIdWithDependencies(dependencyId)
                .orElseThrow(() -> new IllegalArgumentException("Dependency not found"));

        if (hasCircularDependency(task, dependency)) {
            throw new IllegalArgumentException("Circular dependency detected");
        }
        if (!dependency.isCompleted()) {
            throw new IllegalArgumentException("Dependency task must be completed before this task can be added");
        }
        task.getDependencies().add(dependency);
        Task updatedTask = taskRepository.save(task);
        return new TaskDTO(updatedTask.getId(), updatedTask.getTitle(), updatedTask.getDescription(),
                updatedTask.getDueDate(), updatedTask.getPriority(), updatedTask.isCompleted());
    }

    private boolean hasCircularDependency(Task task, Task dependency) {
        Set<Task> visited = new HashSet<>();
        return checkCircular(task, dependency, visited);
    }

    private boolean checkCircular(Task task, Task dependency, Set<Task> visited) {
        if (visited.contains(dependency)) {
            return true;
        }
        visited.add(dependency);

        for (Task dep : dependency.getDependencies()) {
            if (checkCircular(task, dep, visited)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void removeDependency(Long taskId, Long dependencyId) {
        Task task = taskRepository.findByIdWithDependencies(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Task dependency = taskRepository.findByIdWithDependencies(dependencyId)
                .orElseThrow(() -> new IllegalArgumentException("Dependency not found"));
        task.getDependencies().remove(dependency);
        taskRepository.save(task);
    }

    public List<TaskDTO> getTaskDependencies(Long taskId) {
        Task task = taskRepository.findByIdWithDependencies(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        Set<Task> visited = new HashSet<>();
        Set<Task> allDependencies = new HashSet<>();

        collectDependencies(task, visited, allDependencies);

        return allDependencies.stream()
                .map(dep -> new TaskDTO(dep.getId(), dep.getTitle(), dep.getDescription(),
                        dep.getDueDate(), dep.getPriority(), dep.isCompleted()))
                .toList();
    }

    private void collectDependencies(Task task, Set<Task> visited, Set<Task> allDependencies) {
        if (visited.contains(task)) {
            return;
        }
        visited.add(task);
        for (Task dependency : task.getDependencies()) {
            allDependencies.add(dependency);
            collectDependencies(dependency, visited, allDependencies);
        }
    }
}