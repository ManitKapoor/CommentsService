package com.test.intuit.demo.dao;

import com.test.intuit.demo.pojo.CommentRequest;
import com.test.intuit.demo.pojo.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CommentDao {
    List<CommentResponse> findAll(String postId, int page, int size, String userId);

    List<CommentResponse> findAllReplies(String postId, Long commentId, int page, int size, String userId);

    CommentResponse save(CommentRequest commentRequest);

    void likeComment(String postId, Long commentId, String userId);

    void removeLikeComment(String postId, Long commentId, String userId);

    void dislikeComment(String postId, Long commentId, String userId);

    void removeDisLikeComment(String postId, Long commentId, String userId);
}
