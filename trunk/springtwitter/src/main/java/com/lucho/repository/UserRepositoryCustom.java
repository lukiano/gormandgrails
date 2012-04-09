package com.lucho.repository;

import com.lucho.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepositoryCustom {

    User addUser(String username, String password);

    @Transactional(readOnly = true)
    boolean userExists(String username);

    @Transactional(readOnly = true)
    boolean notFollowedBy(User user, User userToFollow);

    void followUser(User user, User userToFollow);

}

