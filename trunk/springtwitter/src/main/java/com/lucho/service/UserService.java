package com.lucho.service;

import com.lucho.domain.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 9/1/11
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserService {
    @Transactional(readOnly = true)
    User getUser(Integer id);

    @Transactional(readOnly = true)
    User getUser(String username);

    @Transactional(readOnly = true)
    boolean userExists(String username);

    @Transactional
    User addUser(String username, String password);

    @Transactional
    void followUser(User user, User userToFollow);
}
