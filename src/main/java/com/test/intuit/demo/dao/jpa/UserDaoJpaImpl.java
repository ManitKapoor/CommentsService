package com.test.intuit.demo.dao.jpa;

import com.test.intuit.demo.dao.UserDao;
import com.test.intuit.demo.dao.jpa.entity.UserEntity;
import com.test.intuit.demo.pojo.User;
import com.test.intuit.demo.dao.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDaoJpaImpl implements UserDao {

    private final UserRepository userRepository;
    @Override
    public User findByUsername(String username) {
        return convertToPojo(userRepository.findByUsername(username));
    }

    private User convertToPojo(UserEntity userEntity) {
        if (Objects.isNull(userEntity)) {
            return null;
        }
        return User.builder()
                .password(userEntity.getPassword())
                .username(userEntity.getUsername())
                .build();
    }

    private UserEntity convertToEntity(User user) {
        return UserEntity.builder()
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userRepository.save(convertToEntity(user));
        return convertToPojo(userEntity);
    }
}
