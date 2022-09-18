package com.test.intuit.demo.service;

import com.test.intuit.demo.dao.CommentDao;
import com.test.intuit.demo.dao.jpa.CommentDaoJpaImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostPartitionService {

    private final CommentDaoJpaImpl commentDaoJpa;

    public CommentDao getCommentDao(String postId) {
         /*
            1. Use postId for horizontal partition, this service would also partition independent of data source
            2. Use consistent hashing to map to data source this allow us to scale independent of postId
         */
        return commentDaoJpa;
    }

}
