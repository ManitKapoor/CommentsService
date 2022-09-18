package com.test.intuit.demo.dao.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="comment_dislikes")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CommentDislike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "comment_id")
    private Long commentId;
    @Column(name = "created_on")
    private Date createdOn;

    @PrePersist
    protected void onCreate() {
        createdOn = new Date();
    }

}
