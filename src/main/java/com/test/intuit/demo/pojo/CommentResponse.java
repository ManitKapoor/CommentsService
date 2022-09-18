package com.test.intuit.demo.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentResponse {
    private String postId;
    private long id;
    private String name;
    private String content;
    private int likes;
    private int dislikes;
    private Long parentId;
    private boolean liked;
    private boolean disliked;
    private String updateOn;
    private String createdOn;
}
