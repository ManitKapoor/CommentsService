package com.test.intuit.demo.controller;

import com.test.intuit.demo.Constants;
import com.test.intuit.demo.pojo.CommentRequest;
import com.test.intuit.demo.pojo.CommentResponse;
import com.test.intuit.demo.pojo.CommentResponseList;
import com.test.intuit.demo.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @GetMapping("/{parentId}/comments")
    public CommentResponseList getAllReplies(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @PathVariable(value = "parentId") String parentId,
            Authentication authentication) {
        return convert(postCommentService.findAll(parentId, page, size, authentication.getName()));
    }

    @PostMapping("/comment")
    public CommentResponse saveComment(Authentication authentication,
                                 @RequestBody @Valid CommentRequest commentRequest) {
        commentRequest.setName(authentication.getName());
        return postCommentService.save(commentRequest);
    }

    @PostMapping("/{id}/like")
    public String likeComment(@PathVariable(value = "id") String commentId,
                            Authentication authentication) {
        postCommentService.likeComment(commentId, authentication.getName());
        return Constants.SUCCESS;
    }

    @PostMapping("/{id}/like/remove")
    public String removeLikeComment(@PathVariable(value = "id") String commentId,
                                  Authentication authentication) {
        postCommentService.removeLikeComment(commentId, authentication.getName());
        return Constants.SUCCESS;
    }

    @PostMapping("/{id}/dislike")
    public String dislikeComment(@PathVariable(value = "id") String commentId,
                               Authentication authentication) {
        postCommentService.dislikeComment(commentId, authentication.getName());
        return Constants.SUCCESS;
    }

    @PostMapping("/{id}/dislike/remove")
    public String removedDislikeComment(@PathVariable(value = "id") String commentId,
                                      Authentication authentication) {
        postCommentService.removeDisLikeComment(commentId, authentication.getName());
        return Constants.SUCCESS;
    }

    private CommentResponseList convert(List<CommentResponse> list) {
        return CommentResponseList.builder().list(list).build();
    }



}
