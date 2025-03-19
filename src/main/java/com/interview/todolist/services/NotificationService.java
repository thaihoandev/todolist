package com.interview.todolist.services;

import com.interview.todolist.models.Notification;
import com.interview.todolist.models.Task;
import com.interview.todolist.repositories.NotificationRepository;
import com.interview.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    // Method to check for upcoming and overdue tasks
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkUpcomingAndOverdueTasks() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<Task> upcomingTasks = taskRepository.findTasksByDueDate(tomorrow);

        for (Task task : upcomingTasks) {
            Notification notification = new Notification();
            notification.setMessage("Task " + task.getTitle() + " is due tomorrow.");
            notification.setTask(task);
            notification.setCreatedAt(LocalDateTime.now()); // Sửa: Dùng LocalDateTime
            notification.setRead(false);
            notificationRepository.save(notification);
        }

        List<Task> overdueTasks = taskRepository.findByDueDateBeforeAndCompletedIsFalse(today);

        for (Task task : overdueTasks) {
            Notification notification = new Notification();
            notification.setMessage("Task " + task.getTitle() + " is overdue.");
            notification.setTask(task);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setRead(false);
            notificationRepository.save(notification);
        }
    }

    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalse();
    }
}