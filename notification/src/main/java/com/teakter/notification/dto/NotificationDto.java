package com.teakter.notification.dto;

import com.teakter.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {

private Long id;
private Long userId;
private String title;
private String message;
private NotificationType type;
private Boolean isRead;
private String targetUrl;

}
