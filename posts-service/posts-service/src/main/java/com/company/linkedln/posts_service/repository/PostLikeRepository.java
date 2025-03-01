package com.company.linkedln.posts_service.repository;

import com.company.linkedln.posts_service.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserIdAndPostId(Long userId, Long postId);


    @Transactional
    void deleteByUserIdAndPostId(long userId, Long postId);
}
