package com.test.intuit.demo.dao.jpa;

import com.test.intuit.demo.dao.CommentDao;
import com.test.intuit.demo.dao.jpa.entity.Comment;
import com.test.intuit.demo.dao.jpa.entity.CommentDislike;
import com.test.intuit.demo.dao.jpa.entity.CommentLike;
import com.test.intuit.demo.pojo.CommentRequest;
import com.test.intuit.demo.pojo.CommentResponse;
import com.test.intuit.demo.dao.jpa.repository.CommentDislikesRepository;
import com.test.intuit.demo.dao.jpa.repository.CommentLikesRepository;
import com.test.intuit.demo.dao.jpa.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentDaoJpaImpl implements CommentDao {

    private final CommentRepository commentRepository;

    private final CommentLikesRepository commentLikesRepository;

    private final CommentDislikesRepository commentDislikesRepository;

    private static final long  TOP_COMMENT_ID = 0L;

    @Override
    public List<CommentResponse> findAll(String postId, int page, int size, String userId) {
        return computeCommentResponses(commentRepository.findAllByPostIdAndParentId(postId, TOP_COMMENT_ID,
                PageRequest.of(page, size, Sort.by("updatedOn").descending())), userId);
    }

    private List<CommentResponse> computeCommentResponses(List<Comment> comments, String userId) {
        List<Long> commentIds = comments.stream().map(Comment::getId).collect(Collectors.toList());
        List<Long> commentLikes = commentLikesRepository.findAllByUserIdAndCommentIdIn(userId, commentIds)
                .stream().map(CommentLike::getCommentId).collect(Collectors.toList());
        List<Long> commentDislikes = commentDislikesRepository.findAllByUserIdAndCommentIdIn(userId, commentIds)
                .stream().map(CommentDislike::getCommentId).collect(Collectors.toList());
        Set<Long> likedCommentIdSet = new HashSet<>(commentLikes);
        Set<Long> dislikedCommentIdSet = new HashSet<>(commentDislikes);
        return comments.stream().map(comment -> CommentResponse.builder()
                .content(comment.getContent())
                .name(comment.getName())
                .parentId(comment.getParentId())
                .id(comment.getId())
                .postId(comment.getPostId())
                .likes(comment.getLikes())
                .dislikes(comment.getDislikes())
                .updateOn(comment.getUpdatedOn().toString())
                .createdOn(comment.getCreatedOn().toString())
                .liked(likedCommentIdSet.contains(comment.getId()))
                .disliked(dislikedCommentIdSet.contains(comment.getId()))
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> findAllReplies(String postId, Long commentId, int page, int size, String userId) {
        return computeCommentResponses(commentRepository.findAllByPostIdAndParentId(postId, commentId, PageRequest.of(page, size, Sort.by("updatedOn").descending())), userId);
    }

    @Override
    public CommentResponse save(CommentRequest commentRequest) {
        return convertEntityToResponse(commentRepository.save(convert(commentRequest)));
    }

    @Transactional
    @Override
    public void likeComment(String postId, Long commentId, String userId) {
        if (!commentLikesRepository.findAllByCommentIdAndUserIdAndPostId(commentId, userId, postId).isEmpty()) {
            return;
        }
        commentRepository.increaseLike(commentId);
        commentLikesRepository.save(CommentLike.builder()
                .commentId(commentId)
                .userId(userId)
                .postId(postId)
                .build());
    }

    @Transactional
    @Override
    public void removeLikeComment(String postId, Long commentId, String userId) {
        if (commentLikesRepository.findAllByCommentIdAndUserIdAndPostId(commentId, userId, postId).isEmpty()) {
            return;
        }
        commentRepository.decreaseLike(commentId);
        commentLikesRepository.deleteAllByCommentIdAndUserIdAndPostId(commentId, userId, postId);
    }

    @Transactional
    @Override
    public void dislikeComment(String postId, Long commentId, String userId) {
        if (!commentDislikesRepository.findAllByCommentIdAndUserIdAndPostId(commentId, userId, postId).isEmpty()) {
            return;
        }
        commentRepository.increaseDisLike(commentId);
        commentDislikesRepository.save(CommentDislike.builder()
                .commentId(commentId)
                .userId(userId)
                .postId(postId)
                .build());
    }

    @Transactional
    @Override
    public void removeDisLikeComment(String postId, Long commentId, String userId) {
        if (commentDislikesRepository.findAllByCommentIdAndUserIdAndPostId(commentId, userId, postId).isEmpty()) {
            return;
        }
        commentRepository.decreaseDisLike(commentId);
        commentDislikesRepository.deleteAllByCommentIdAndUserIdAndPostId(commentId, userId, postId);
    }

    private CommentResponse convertEntityToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .name(comment.getName())
                .likes(comment.getLikes())
                .dislikes(comment.getDislikes())
                .build();
    }


    private Comment convert( CommentRequest commentRequest) {
        return Comment.builder()
                .id(commentRequest.getId())
                .postId(commentRequest.getPostId())
                .likes(commentRequest.getLikes())
                .content(commentRequest.getContent())
                .name(commentRequest.getName())
                .dislikes(commentRequest.getDislikes())
                .parentId(commentRequest.getParentId())
                .build();
    }
}
