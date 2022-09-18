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

    @GetMapping("/{postId}/{id}/comments")
    public CommentResponseList getAllReplies(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @PathVariable(value = "postId") String postId,
            Authentication authentication,
            @PathVariable(value = "id") Long commentId) {
        return convert(postCommentService.findAll(postId, commentId, page, size, authentication.getName()));
    }

    @GetMapping("/{postId}/comments")
    public CommentResponseList getAllComments(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            Authentication authentication,
            @PathVariable(value = "postId") String postId) {
        return convert(postCommentService.findAll(postId, page, size, authentication.getName()));
    }

    @PostMapping("/comment")
    public CommentResponse saveComment(Authentication authentication,
                                 @RequestBody @Valid CommentRequest commentRequest) {
        commentRequest.setName(authentication.getName());
        return postCommentService.save(commentRequest);
    }

    @PostMapping("/{postId}/{id}/like")
    public String likeComment(@PathVariable(value = "postId") String postId,
                            @PathVariable(value = "id") Long commentId,
                            Authentication authentication) {
        postCommentService.likeComment(postId, commentId, authentication.getName());
        return Constants.SUCCESS;
    }

    @PostMapping("/{postId}/{id}/like/remove")
    public String removeLikeComment(@PathVariable(value = "postId") String postId,
                                  @PathVariable(value = "id") Long commentId,
                                  Authentication authentication) {
        postCommentService.removeLikeComment(postId, commentId, authentication.getName());
        return Constants.SUCCESS;
    }

    @PostMapping("/{postId}/{id}/dislike")
    public String dislikeComment(@PathVariable(value = "postId") String postId,
                                @PathVariable(value = "id") Long commentId,
                               Authentication authentication) {
        postCommentService.dislikeComment(postId, commentId, authentication.getName());
        return Constants.SUCCESS;
    }

    @PostMapping("/{postId}/{id}/dislike/remove")
    public String removedDislikeComment(@PathVariable(value = "postId") String postId,
                                      @PathVariable(value = "id") Long commentId,
                                      Authentication authentication) {
        postCommentService.removeDisLikeComment(postId, commentId, authentication.getName());
        return Constants.SUCCESS;
    }

    private CommentResponseList convert(List<CommentResponse> list) {
        return CommentResponseList.builder().list(list).build();
    }



}
