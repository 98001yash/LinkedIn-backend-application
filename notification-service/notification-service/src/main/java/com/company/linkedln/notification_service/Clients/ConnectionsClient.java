package com.company.linkedln.notification_service.Clients;



import com.company.linkedln.notification_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections")
public interface ConnectionsClient {

    @GetMapping("/core/first-connections")
     List<PersonDto> getFirstConnections(@RequestHeader("X-User-Id")Long userId);


}
