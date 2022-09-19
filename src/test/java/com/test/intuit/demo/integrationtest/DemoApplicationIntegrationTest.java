package com.test.intuit.demo.integrationtest;

import com.test.intuit.demo.pojo.CommentRequest;
import com.test.intuit.demo.pojo.CommentResponse;
import com.test.intuit.demo.pojo.CommentResponseList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = DemoApplicationIntegrationTestInitializer.class)
public class DemoApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String authHeader = "Authorization";
    private String authHeaderValue = "Basic dGVzdDp0ZXN0cGFzc3dvcmQ=";

    private String wrongAuthHeaderValue = "Basic dGVzdGFzZDpkZGRhYWFhYQ==";

    private RequestEntity<CommentRequest> postRequest(String url, CommentRequest commentRequest) {
        return RequestEntity.post(url).header(authHeader, authHeaderValue).body(commentRequest);
    }

    private RequestEntity<Void> postRequest(String url) {
        return RequestEntity.post(url).header(authHeader, authHeaderValue).build();
    }

    private RequestEntity<Void> getRequest(String url) {
        return RequestEntity.get(url).header(authHeader, authHeaderValue).build();
    }

    @Test
    public void saveCommentTest() {
        String parentId = "SamplePostId";
        CommentRequest commentRequest = CommentRequest.builder().parentId(parentId).name("test").content("Test comment here").build();
        ResponseEntity<CommentResponse> responseEntity = restTemplate.exchange(postRequest("/api/v1/comment", commentRequest), CommentResponse.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        CommentResponse returnedComment = responseEntity.getBody();
        Assertions.assertNotNull(returnedComment);
        Assertions.assertEquals(commentRequest.getName(), returnedComment.getName());
        Assertions.assertEquals(commentRequest.getContent(), returnedComment.getContent());

        String commentId = returnedComment.getId();
        CommentRequest editCommentRequest = CommentRequest.builder().id(commentId).parentId(parentId).name("test").content("Edit Test comment here").build();
        responseEntity = restTemplate.exchange(postRequest("/api/v1/comment", editCommentRequest), CommentResponse.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        CommentResponse returnedEditedComment = responseEntity.getBody();
        Assertions.assertNotNull(returnedEditedComment);
        Assertions.assertEquals(editCommentRequest.getName(), returnedEditedComment.getName());
        Assertions.assertEquals(editCommentRequest.getContent(), returnedEditedComment.getContent());
        Assertions.assertEquals(commentId, returnedEditedComment.getId());
    }

    @Test
    public void runServiceForManualDebug() {
        try {
            Thread.sleep(9000000000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void fetchCommentsByPageTest() {
        String parentId = "SamplePostId";
        String url = String.format("/api/v1/%s/comments?page=0&size=10", parentId);
        ResponseEntity<CommentResponseList> responseEntity =
                restTemplate.exchange(getRequest(url), CommentResponseList.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void fetchCommentsByPageTestAuthFail() {
        String postId = "SamplePostId";
        String url = String.format("/api/v1/%s/comments?page=0&size=10", postId);
        ResponseEntity<CommentResponseList> responseEntity =
                restTemplate.exchange(RequestEntity.get(url).header(authHeader, wrongAuthHeaderValue).build(), CommentResponseList.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }


    @Test
    public void likeCommentTest() {
        String parentId = "SamplePostId";
        String commentId = "MainId";
        String url = String.format("/api/v1/%s/like", commentId);
        ResponseEntity<String> responseEntity = restTemplate.exchange(postRequest(url), String.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertTrue(checkCommentLike(parentId, commentId, true));

        url = String.format("/api/v1/%s/like/remove", commentId);
        responseEntity = restTemplate.exchange(postRequest(url), String.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertTrue(checkCommentLike(parentId, commentId, false));
    }


    @Test
    public void dislikeCommentTest() {
        String parentId = "SamplePostId";
        String commentId = "MainId";
        String url = String.format("/api/v1/%s/dislike", commentId);
        ResponseEntity<String> responseEntity = restTemplate.exchange(postRequest(url), String.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertTrue(checkCommentDislike(parentId, commentId, true));

        url = String.format("/api/v1/%s/dislike/remove", commentId);
        responseEntity = restTemplate.exchange(postRequest(url), String.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertTrue(checkCommentDislike(parentId, commentId, false));
    }

    private boolean checkCommentDislike(String parentId, String commentId, boolean value) {
        String url = String.format("/api/v1/%s/comments?page=0&size=10", parentId);
        ResponseEntity<CommentResponseList> responseEntity =
                restTemplate.exchange(getRequest(url), CommentResponseList.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<CommentResponse> commentResponses = responseEntity.getBody().getList();
        for (CommentResponse commentResponse: commentResponses) {
            System.out.println(commentResponse.toString());
            if (commentResponse.getId().equals(commentId)) {
                return commentResponse.isDisliked() == value;
            }
        }
        return false;
    }

    public boolean checkCommentLike(String parentId, String commentId, boolean value) {
        String url = String.format("/api/v1/%s/comments?page=0&size=10", parentId);
        ResponseEntity<CommentResponseList> responseEntity =
                restTemplate.exchange(getRequest(url), CommentResponseList.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<CommentResponse> commentResponses = responseEntity.getBody().getList();
        for (CommentResponse commentResponse: commentResponses) {
            System.out.println(commentResponse.toString());
            if (commentResponse.getId().equals(commentId)) {
                return commentResponse.isLiked() == value;
            }
        }
        return false;
    }


}