package com.company.linkedln.connections_service.service;


import com.company.linkedln.connections_service.auth.UserContextHolder;
import com.company.linkedln.connections_service.entities.Person;
import com.company.linkedln.connections_service.event.AcceptConnectionRequestEvent;
import com.company.linkedln.connections_service.event.SendConnectionRequestEvent;
import com.company.linkedln.connections_service.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsService {


    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendRequestKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptRequestEventKafkaTemplate;

    public List<Person> getFirstDegreeConnections(){
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Getting first degree connections for user with idL: {}", userId);
        return  personRepository.getFirstDegreeConnection(userId);
    }


    public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();

        log.info("Trying to send the connection request, sender:  {}, receiver: {}",senderId, receiverId);
         if(senderId.equals(receiverId)){
             throw new RuntimeException("Both sender and receiver are the same user");
         }

        boolean alreadySendRequest = personRepository.connectionRequestExists(senderId,receiverId);
        if(alreadySendRequest){
            throw new RuntimeException("Connection request already exists, cannot send again");
        }
        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if(alreadyConnected){
            throw new RuntimeException("already connected users, cannot add connections again...");
        }

        log.info("successfully send the connection request...!!");
       personRepository.addConnectionRequest(senderId,receiverId);

       SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
               .senderId(senderId)
               .receiverId(receiverId)
               .build();

       sendRequestKafkaTemplate.send("send-connection-request-topic",sendConnectionRequestEvent);
        return true;
    }

    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId  =  UserContextHolder.getCurrentUserId();
        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId, receiverId);
        if(!connectionRequestExists){
            throw new RuntimeException("No connections request exists to accept");
        }

        log.info("Accepting the connection request...!!");
     personRepository.acceptConnectionRequest(senderId, receiverId);

     log.info("Successfully accepted the connection request, sender: {}, receiver: {}",senderId, receiverId);
        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        acceptRequestEventKafkaTemplate.send("accept-connection-request-topic",acceptConnectionRequestEvent);
      return true;
    }

    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId, receiverId);
        if(!connectionRequestExists){
            throw new RuntimeException("No connections request exists to reject");
        }
        log.info("Rejecting the connection request...!!");
        personRepository.rejectConnectionRequest(senderId, receiverId);

        return true;
    }
}
