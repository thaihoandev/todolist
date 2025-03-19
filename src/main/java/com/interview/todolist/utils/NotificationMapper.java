package com.interview.todolist.utils;

import com.interview.todolist.dtos.NotificationDTO;
import com.interview.todolist.models.Notification;

public class NotificationMapper {

    public static NotificationDTO toDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getTask().getTitle(),
                notification.getCreatedAt(),
                notification.isRead()
        );
    }
}
