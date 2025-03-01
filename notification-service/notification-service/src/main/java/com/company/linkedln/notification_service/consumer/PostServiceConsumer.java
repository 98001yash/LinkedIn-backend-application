package com.company.linkedln.notification_service.consumer;


import com.company.linkedln.notification_service.Clients.ConnectionsClient;
import com.company.linkedln.notification_service.NotificationServiceApplication;
import com.company.linkedln.notification_service.dto.PersonDto;
import com.company.linkedln.notification_service.entity.Notification;
import com.company.linkedln.notification_service.repository.NotificationRepository;
import com.company.linkedln.notification_service.service.SendNotification;
import com.company.linkedln.posts_service.event.PostCreatedEvent;
import com.company.linkedln.posts_service.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final NotificationRepository notificationRepository;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        log.info("Sending notifications: handlePostCreated: {}", postCreatedEvent);

        try {
            List<PersonDto> connections = connectionsClient.getFirstConnections(postCreatedEvent.getCreaterId());
            if (connections.isEmpty()) {
                log.info("No connections found for user {}", postCreatedEvent.getCreaterId());
                return;
            }

            for (PersonDto connection : connections) {
               sendNotification.send(connection.getUserId(), "Your connection "+postCreatedEvent.getCreaterId());
            }
        } catch (Exception e) {
            log.error("Failed to fetch connections for user {}: {}", postCreatedEvent.getCreaterId(), e.getMessage());
        }
    }

    @KafkaListener(topics = "post-like-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent) {
        log.info("Sending notifications: handlePostLiked: {}", postLikedEvent);

        String message = String.format("Your post, %d has been liked by %d", postLikedEvent.getPostId(), postLikedEvent.getLikedByUserId());
        sendNotification.send(postLikedEvent.getCreatorId(), message);
    }
}
