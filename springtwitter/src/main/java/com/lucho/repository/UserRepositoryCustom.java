package com.lucho.repository;

import com.lucho.domain.User;

public interface UserRepositoryCustom {

    User addUser(String username, String password);

    boolean userExists(String username);

    boolean notFollowedBy(User user, User userToFollow);

    void followUser(User user, User userToFollow);

}

