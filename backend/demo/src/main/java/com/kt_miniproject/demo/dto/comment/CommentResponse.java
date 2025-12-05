package com.kt_miniproject.demo.dto.comment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private Long userId;
    private String userName;
    private Long bookId;
    private LocalDateTime createdAt;
}
