package com.kt_miniproject.demo.domain.common;

import com.kt_miniproject.demo.domain.comment.Comment;

public interface CommentService {

    // 댓글 작성
    Comment createComment(Long bookId, Long userId, String content);

    // 댓글 삭제
    void deleteComment(Long commentId);

    // 댓글 추천(좋아요)
    Comment likeComment(Long commentId);
}
