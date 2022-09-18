package com.test.intuit.demo.service;

import com.test.intuit.demo.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserAuthDetailsService implements UserDetailsService {

    private final UserPartitionService userPartitionService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAuthDetailsService(UserPartitionService userPartitionService,
                                  PasswordEncoder passwordEncoder) {
        this.userPartitionService = userPartitionService;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        return userPartitionService.getUserDao(user.getUsername()).save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userPartitionService.getUserDao(username).findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .passwordEncoder(passwordEncoder::encode)
                .authorities("user")
                .build();
    }
}