package com.company.linkedln.posts_service.event;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreatedEvent {

    private Long createrId;
    private String content;
    private Long postId;

}
