package com.test.intuit.demo.service;

import com.test.intuit.demo.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPartitionService {

    private final UserDao userDao;

    public UserDao getUserDao(String username) {
         /*
            1. Use username for horizontal partition, this service would also partition independent of data source
            2. Use consistent hashing to map to data source this allow us to scale independent of username
         */
        return userDao;
    }

}
