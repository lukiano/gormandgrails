package com.lucho.dao;

import com.lucho.domain.User;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 9/1/11
 * Time: 6:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserDao {
    User getUser(Integer id);

    User getUser(String username);

    boolean userExists(String username);

    User addUser(String username, String password);

    void followUser(User user, User userToFollow);
}
