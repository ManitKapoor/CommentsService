package com.test.intuit.demo.dao;

import com.test.intuit.demo.pojo.User;
import org.springframework.stereotype.Service;

@Service
public interface UserDao {
    User findByUsername(String username);

    User save(User userEntity);
}
