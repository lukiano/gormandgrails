package com.lucho.service;

import com.lucho.domain.User;

public interface UserService {

    User getUser(Integer id);

    User getUser(String username);

    boolean userExists(String username);

    User addUser(String username, String password);

    void followUser(User user, User userToFollow);

    void refreshFollowersFor(Integer ownerId);

    boolean shouldRefresh(User user);
}
