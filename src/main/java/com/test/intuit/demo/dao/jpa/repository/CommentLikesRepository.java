package com.test.intuit.demo.dao.jpa.repository;

import com.test.intuit.demo.dao.jpa.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentLikesRepository extends JpaRepository<CommentLike, Long> {
    List<CommentLike> findAllByCommentIdAndUserId(String commentId, String userId);

    @Modifying
    void deleteAllByCommentIdAndUserId(String commentId, String userId);

    List<CommentLike> findAllByUserIdAndCommentIdIn(String userId, List<String> commentIds);
}