package com.kt_miniproject.demo.service;

import com.kt_miniproject.demo.dto.comment.CommentCreateRequest;
import com.kt_miniproject.demo.dto.comment.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(Long bookId, Long userId, CommentCreateRequest request);

    List<CommentResponse> getComments(Long bookId);

    void deleteComment(Long bookId, Long commentId);

}
