package com.company.linkedln.posts_service.service;


import com.company.linkedln.posts_service.auth.UserContextHolder;
import com.company.linkedln.posts_service.entity.Post;
import com.company.linkedln.posts_service.entity.PostLike;
import com.company.linkedln.posts_service.event.PostLikedEvent;
import com.company.linkedln.posts_service.exceptions.BadRequestException;
import com.company.linkedln.posts_service.exceptions.ResourceNotFoundException;
import com.company.linkedln.posts_service.repository.PostLikeRepository;
import com.company.linkedln.posts_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    public void likePost(Long postId){
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to like the post with id: {}",postId);

        Post post = postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post not found with id: "+postId));


        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(alreadyLiked) throw new BadRequestException("cannot like the same post again.");

        PostLike postLike = new PostLike();

        postLike.setPostId(postId);
        postLike.setUserId(userId);

        postLikeRepository.save(postLike);
        log.info("post with id: {} liked successfully",postId);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .likedByUserId(userId)
                .creatorId(post.getUserId()).build();

        kafkaTemplate.send("post-like-topic",postId,postLikedEvent);

    }

    public void unlikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to delete the post with id: {}", postId);
        boolean exists = postRepository.existsById(postId);
        if(!exists)throw new ResourceNotFoundException("Post not found with id: "+postId);

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!alreadyLiked) throw new BadRequestException("cannot unlike the post which is not liked.");

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);

        log.info("post with id: {} unliked successfully", postId);
    }
}
