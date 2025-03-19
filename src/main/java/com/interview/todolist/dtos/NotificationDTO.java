package com.interview.todolist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationDTO {

    private Long id;
    private String message;
    private String taskTitle;
    private LocalDateTime createdAt;
    private boolean isRead;
}
