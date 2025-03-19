package com.interview.todolist.repositories;

import com.interview.todolist.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.interview.todolist.models.Notification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByIsReadFalse();

    Optional<Notification> findById(Long id);

    List<Notification> findByTaskId(Long taskId);


}
