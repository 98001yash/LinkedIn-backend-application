package com.company.linkedln.notification_service.consumer;

import com.company.linkedln.connections_service.event.AcceptConnectionRequestEvent;
import com.company.linkedln.connections_service.event.SendConnectionRequestEvent;
import com.company.linkedln.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {

    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connections-request-topic")
    public void handleSendConnectionsRequest(SendConnectionRequestEvent sendConnectionRequestEvent){
     String message =
             "You have received the connection request from user with id: %d"+sendConnectionRequestEvent.getSenderId();
     sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message);
    }

    @KafkaListener(topics = "accept-connections-request-topic")
    public void handleAcceptConnectionsRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        String message =
                "Your connection request is accepted by the  user with id: %d" + acceptConnectionRequestEvent.getSenderId();
        sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);
    }



}
