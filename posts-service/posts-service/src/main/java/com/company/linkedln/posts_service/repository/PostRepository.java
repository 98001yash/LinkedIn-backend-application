package com.company.linkedln.posts_service.repository;

import com.company.linkedln.posts_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {


    List<Post> findByUserId(Long userId);
}
