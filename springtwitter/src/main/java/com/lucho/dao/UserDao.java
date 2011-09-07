package com.lucho.dao;

import com.lucho.domain.User;

public interface UserDao {

    User getUser(Integer id);

    User getUser(String username);

    boolean userExists(String username);

    User addUser(String username, String password);

    boolean notFollowedBy(User user, User userToFollow);

    void followUser(User user, User userToFollow);
}
