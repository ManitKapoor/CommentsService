package com.test.intuit.demo.dao.jpa.entity;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="comment")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Comment implements Serializable {

    @Id
    private String id;

    @Column(name = "created_user_id")
    private String name;

    private String content;

    @Column(name = "parent_id")
    private String parentId;

    private Integer likes;
    private Integer dislikes;
    @Column(name = "created_on", updatable = false)
    private Date createdOn;
    @Column(name = "updated_on")
    private Date updatedOn;

    @PrePersist
    protected void onCreate() {
        createdOn = new Date();
        updatedOn = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = new Date();
    }
}
