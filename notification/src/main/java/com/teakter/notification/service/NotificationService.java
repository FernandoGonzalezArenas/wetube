package com.teakter.notification.service;

import com.teakter.notification.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

List<NotificationDto> getUserNotifications(Long lastId, int limit);

Long getUnreadCount();

void markAsRead(Long id);

}
