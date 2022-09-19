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


    public List<CommentResponse> findAll(String parentId, int page, int size, String username) {
        return postPartitionService.getCommentDao(parentId).findAllReplies(parentId, page, size, username);
    }

    public CommentResponse save(CommentRequest commentRequest) {
        return postPartitionService.getCommentDao(commentRequest.getParentId()).save(commentRequest);
    }

    public void likeComment(String  commentId, String username) {
        postPartitionService.getCommentDao(commentId).actionComment(commentId, username, true);
    }

    public void removeLikeComment(String  commentId, String username) {
        postPartitionService.getCommentDao(commentId).removeActionComment(commentId, username);
    }

    public void dislikeComment(String  commentId, String username) {
        postPartitionService.getCommentDao(commentId).actionComment(commentId, username, false);

    }

    public void removeDisLikeComment(String  commentId, String username) {
        postPartitionService.getCommentDao(commentId).removeActionComment(commentId, username);
    }
}
