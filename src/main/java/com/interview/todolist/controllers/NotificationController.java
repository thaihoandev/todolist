package com.interview.todolist.controllers;

import com.interview.todolist.dtos.NotificationDTO;
import com.interview.todolist.models.Notification;
import com.interview.todolist.repositories.NotificationRepository;
import com.interview.todolist.services.NotificationService;
import com.interview.todolist.utils.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    // Get all unread notifications
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() {
        List<Notification> unreadNotifications = notificationRepository.findByIsReadFalse();
        List<NotificationDTO> notificationDTOs = unreadNotifications.stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(notificationDTOs, HttpStatus.OK);
    }

    // Mark notification as read
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
        return new ResponseEntity<>(NotificationMapper.toDTO(notification), HttpStatus.OK);
    }
}
