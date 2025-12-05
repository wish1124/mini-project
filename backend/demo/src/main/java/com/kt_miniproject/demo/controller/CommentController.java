package com.kt_miniproject.demo.controller;

import com.kt_miniproject.demo.dto.comment.CommentCreateRequest;
import com.kt_miniproject.demo.dto.comment.CommentResponse;
import com.kt_miniproject.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")   // 🔹 여기만 /api/books 로 단순하게
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 1. 댓글 생성
     * POST /api/books/{bookId}/comments?userId=1
     */
    @PostMapping("/{bookId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long bookId,
            @RequestParam Long userId,
            @RequestBody CommentCreateRequest request
    ) {
        CommentResponse response = commentService.createComment(bookId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 2. 특정 도서의 댓글 목록 조회
     * GET /api/books/{bookId}/comments
     */
    @GetMapping("/{bookId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long bookId
    ) {
        List<CommentResponse> comments = commentService.getComments(bookId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 3. 댓글 삭제
     * DELETE /api/books/{bookId}/comments/{commentId}
     */
    @DeleteMapping("/{bookId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long bookId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(bookId, commentId);
        return ResponseEntity.noContent().build();
    }
}
