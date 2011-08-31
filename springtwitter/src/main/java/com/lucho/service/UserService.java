package com.lucho.service;

import com.lucho.dao.UserDao;
import com.lucho.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/30/11
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
@Service("userService")
public class UserService implements UserDetailsService {

    private UserDao userDao;

    @Autowired
    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user = this.getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public User getUser(String username) {
        return this.getUserDao().getUser(username);
    }

    @Transactional
    public User addUser(final String username, final String password) {
        return this.addUser(username, password);
    }
}
