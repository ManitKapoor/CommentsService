package com.test.intuit.demo.service;

import com.test.intuit.demo.pojo.CommentRequest;
import com.test.intuit.demo.pojo.CommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostCommentService {

    private final PostPartitionService postPartitionService;

    public List<CommentResponse> findAll(String postId, int page, int size, String username) {
        return postPartitionService.getCommentDao(postId).findAll(postId, page, size, username);
    }

    public List<CommentResponse> findAll(String postId, Long commentId, int page, int size, String username) {
        return postPartitionService.getCommentDao(postId).findAllReplies(postId, commentId, page, size, username);
    }

    public CommentResponse save(CommentRequest commentRequest) {
        return postPartitionService.getCommentDao(commentRequest.getPostId()).save(commentRequest);
    }

    public void likeComment(String postId, Long commentId, String username) {
        postPartitionService.getCommentDao(postId).likeComment(postId, commentId, username);
    }

    public void removeLikeComment(String postId, Long commentId, String username) {
        postPartitionService.getCommentDao(postId).removeLikeComment(postId, commentId, username);
    }

    public void dislikeComment(String postId, Long commentId, String username) {
        postPartitionService.getCommentDao(postId).dislikeComment(postId, commentId, username);

    }

    public void removeDisLikeComment(String postId, Long commentId, String username) {
        postPartitionService.getCommentDao(postId).removeDisLikeComment(postId, commentId, username);
    }
}
