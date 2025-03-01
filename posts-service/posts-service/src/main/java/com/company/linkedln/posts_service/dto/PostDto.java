package com.company.linkedln.posts_service.dto;



import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostDto {

    public Long id;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
}
