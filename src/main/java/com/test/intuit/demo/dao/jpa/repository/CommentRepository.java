package com.test.intuit.demo.dao.jpa.repository;

import com.test.intuit.demo.dao.jpa.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    List<Comment> findAllByPostIdAndParentId(String postId, long parentId, Pageable pageable);

    @Modifying
    @Query("update Comment set likes = likes + 1 where id = :id")
    void increaseLike(@Param("id") long commentId);

    @Modifying
    @Query("update Comment set dislikes = dislikes + 1 where id = :id")
    void increaseDisLike(@Param("id") long commentId);

    @Modifying
    @Query("update Comment set likes = likes - 1 where id = :id")
    void decreaseLike(@Param("id") long commentId);

    @Modifying
    @Query("update Comment set dislikes = dislikes - 1 where id = :id")
    void decreaseDisLike(@Param("id") long commentId);
}
