package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.notification.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationService {

    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String DESTINATION_PREFIX = "/queue/private-notifications";

    public void sendNotificationToUser(NotificationDTO notificationDTO) {
        final String username = userService.getUserById(notificationDTO.getUserId()).getUsername();
        messagingTemplate.convertAndSendToUser(username, DESTINATION_PREFIX, notificationDTO);
        log.info("WebSocket Notification sent to user {}", username);
    }

}
