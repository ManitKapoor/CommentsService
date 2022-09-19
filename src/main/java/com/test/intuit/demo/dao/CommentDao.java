package com.test.intuit.demo.dao;

import com.test.intuit.demo.pojo.CommentRequest;
import com.test.intuit.demo.pojo.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CommentDao {

    List<CommentResponse> findAllReplies(String parentId, int page, int size, String userId);

    CommentResponse save(CommentRequest commentRequest);

    void actionComment(String commentId, String userId, boolean action);

    void removeActionComment(String commentId, String userId);
}
