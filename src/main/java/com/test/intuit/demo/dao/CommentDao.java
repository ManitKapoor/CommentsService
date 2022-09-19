package com.test.intuit.demo.dao;

import com.test.intuit.demo.pojo.CommentRequest;
import com.test.intuit.demo.pojo.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CommentDao {

    List<CommentResponse> findAllReplies(String parentId, int page, int size, String userId);

    CommentResponse save(CommentRequest commentRequest);

    void likeComment(String commentId, String userId);

    void removeLikeComment(String commentId, String userId);

    void dislikeComment(String commentId, String userId);

    void removeDisLikeComment(String commentId, String userId);
}
