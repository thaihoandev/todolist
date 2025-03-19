package com.interview.todolist.repositories;

import com.interview.todolist.dtos.TaskDTO;
import com.interview.todolist.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT new com.interview.todolist.dtos.TaskDTO(t.id, t.title, t.description, t.dueDate, t.priority, t.completed) FROM Task t WHERE UPPER(t.priority) = UPPER(:priority)")
    Page<TaskDTO> findByPriority(@Param("priority") String priority, Pageable pageable);

    @Query("SELECT new com.interview.todolist.dtos.TaskDTO(t.id, t.title, t.description, t.dueDate, t.priority, t.completed) FROM Task t WHERE t.completed = :completed")
    Page<TaskDTO> findByCompleted(@Param("completed") boolean completed, Pageable pageable);

    @Query("SELECT new com.interview.todolist.dtos.TaskDTO(t.id, t.title, t.description, t.dueDate, t.priority, t.completed) FROM Task t WHERE t.dueDate = :dueDate")
    Page<TaskDTO> findByDueDate(@Param("dueDate") LocalDate dueDate, Pageable pageable);

    @Query("SELECT new com.interview.todolist.dtos.TaskDTO(t.id, t.title, t.description, t.dueDate, t.priority, t.completed) FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<TaskDTO> findByTitleContaining(@Param("title") String title, Pageable pageable);

    @Query("SELECT new com.interview.todolist.dtos.TaskDTO(t.id, t.title, t.description, t.dueDate, t.priority, t.completed) FROM Task t")
    Page<TaskDTO> findAllDTO(Pageable pageable);

    @Query("SELECT new com.interview.todolist.dtos.TaskDTO(t.id, t.title, t.description, t.dueDate, t.priority, t.completed) FROM Task t WHERE t.id = :id")
    Optional<TaskDTO> findDTOById(@Param("id") Long id);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.dependencies WHERE t.id = :id")
    Optional<Task> findByIdWithDependencies(@Param("id") Long id);
}