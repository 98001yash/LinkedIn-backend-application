package com.company.linkedln.posts_service.event;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLikedEvent {

    private Long postId;
    private Long creatorId;
    private Long likedByUserId;
}
