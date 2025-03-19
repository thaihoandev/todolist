package com.interview.todolist.repositories;

import com.interview.todolist.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    Page<Task> findByPriority(String priority, Pageable pageable);
    Page<Task> findByCompleted(boolean completed, Pageable pageable);
    Page<Task> findByDueDate(LocalDate dueDate, Pageable pageable);
    Page<Task> findByTitleContaining(String title, Pageable pageable);

}
