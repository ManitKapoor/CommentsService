package com.test.intuit.demo.dao.jpa.repository;


import com.test.intuit.demo.dao.jpa.entity.CommentDislike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDislikesRepository extends JpaRepository<CommentDislike, Long> {

    List<CommentDislike> findAllByCommentIdAndUserId(String commentId, String userId);

    @Modifying
    void deleteAllByCommentIdAndUserId(String commentId, String userId);

    List<CommentDislike> findAllByUserIdAndCommentIdIn(String userId, List<String> commentIds);

}