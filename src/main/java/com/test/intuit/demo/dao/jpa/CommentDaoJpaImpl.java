package com.test.intuit.demo.dao.jpa;

import com.test.intuit.demo.dao.CommentDao;
import com.test.intuit.demo.dao.jpa.entity.Comment;
import com.test.intuit.demo.dao.jpa.entity.CommentLike;
import com.test.intuit.demo.pojo.CommentRequest;
import com.test.intuit.demo.pojo.CommentResponse;
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


    private List<CommentResponse> computeCommentResponses(List<Comment> comments, String userId) {
        List<String> commentIds = comments.stream().map(Comment::getId).collect(Collectors.toList());
        List<CommentLike> commentLikes = commentLikesRepository.findAllByUserIdAndCommentIdIn(userId, commentIds);
        HashMap<String, CommentLike> likedCommentMap = new HashMap<>();
        commentLikes.forEach(commentLike -> likedCommentMap.put(commentLike.getCommentId(), commentLike));
        return comments.stream().map(comment -> {
            boolean liked = false;
            boolean disliked = false;
            if (likedCommentMap.containsKey(comment.getId())) {
                boolean value = Boolean.parseBoolean(likedCommentMap.get(comment.getId()).getValue());
                liked = value;
                disliked = !value;
            }
            return CommentResponse.builder()
                    .content(comment.getContent())
                    .name(comment.getName())
                    .parentId(comment.getParentId())
                    .id(comment.getId())
                    .likes(comment.getLikes())
                    .dislikes(comment.getDislikes())
                    .updateOn(comment.getUpdatedOn().toString())
                    .createdOn(comment.getCreatedOn().toString())
                    .liked(liked)
                    .disliked(disliked)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> findAllReplies(String parentId, int page, int size, String userId) {
        return computeCommentResponses(commentRepository.findAllByParentId(parentId, PageRequest.of(page, size, Sort.by("updatedOn").descending())), userId);
    }

    @Override
    public CommentResponse save(CommentRequest commentRequest) {
        return convertEntityToResponse(commentRepository.save(convert(commentRequest)));
    }

    @Transactional
    @Override
    public void actionComment(String commentId, String userId, boolean value) {
        if (!commentLikesRepository.findAllByCommentIdAndUserId(commentId, userId).isEmpty()) {
            return;
        }
        commentRepository.increaseLike(commentId);
        commentLikesRepository.save(CommentLike.builder()
                .commentId(commentId)
                .userId(userId)
                .value(String.valueOf(value))
                .build());
    }

    @Transactional
    @Override
    public void removeActionComment(String commentId, String userId) {
        if (commentLikesRepository.findAllByCommentIdAndUserId(commentId, userId).isEmpty()) {
            return;
        }
        commentRepository.decreaseLike(commentId);
        commentLikesRepository.deleteAllByCommentIdAndUserId(commentId, userId);
    }

    private CommentResponse convertEntityToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .name(comment.getName())
                .likes(comment.getLikes())
                .dislikes(comment.getDislikes())
                .build();
    }


    private Comment convert( CommentRequest commentRequest) {
        String id = commentRequest.getId();
        if (Objects.isNull(id)) {
            id = UUID.randomUUID().toString();
        }
        return Comment.builder()
                .id(id)
                .likes(commentRequest.getLikes())
                .content(commentRequest.getContent())
                .name(commentRequest.getName())
                .dislikes(commentRequest.getDislikes())
                .parentId(commentRequest.getParentId())
                .build();
    }
}
