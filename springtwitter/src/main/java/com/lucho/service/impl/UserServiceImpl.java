package com.lucho.service.impl;

import com.lucho.dao.UserDao;
import com.lucho.domain.User;
import com.lucho.service.UserService;
import org.infinispan.util.concurrent.ConcurrentHashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service("userService")
public final class UserServiceImpl implements UserDetailsService, UserService {

    private final ConcurrentHashSet<Integer> usersToBeRefreshed = new ConcurrentHashSet<Integer>();

    private UserDao userDao;

    @Autowired
    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        User user = this.getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
        GrantedAuthority gai = new SimpleGrantedAuthority("ROLE_USER");
        user.setAuthorities(Collections.singletonList(gai));
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(final Integer id) {
        return this.userDao.getUser(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(String username) {
        return this.userDao.getUser(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(final String username) {
        return this.userDao.userExists(username);
    }

    @Override
    @Transactional
    public User addUser(final String username, final String password) {
        if (this.userDao.userExists(username)) {
            return null;
        }
        return this.userDao.addUser(username, password);
    }

    @Override
    @Transactional
    public void followUser(final User user, final User userToFollow) {
        if (this.userDao.notFollowedBy(user, userToFollow)) {
            this.userDao.followUser(user, userToFollow);
        }
    }

    @Override
    @Transactional
    public void refreshFollowersFor(Integer ownerId) {
        User user = this.userDao.getUser(ownerId);
        List<User> followedByList = user.getFollowedBy();
        for (User follower : followedByList) {
               usersToBeRefreshed.add(follower.getId());
        }
    }

    @Override
    public boolean shouldRefresh(User user) {
        return usersToBeRefreshed.remove(user.getId());
    }

}
