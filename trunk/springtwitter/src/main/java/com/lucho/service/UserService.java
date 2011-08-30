package com.lucho.service;

import com.lucho.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/30/11
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
