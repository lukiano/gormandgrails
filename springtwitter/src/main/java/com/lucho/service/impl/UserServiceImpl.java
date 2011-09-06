package com.lucho.service.impl;

import com.lucho.dao.UserDao;
import com.lucho.domain.User;
import com.lucho.service.UserService;
import org.infinispan.util.concurrent.ConcurrentHashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    private ConcurrentHashSet<Integer> usersToBeRefreshed = new ConcurrentHashSet<Integer>();

    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    @Autowired
    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException {
        User user = this.getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
        this.addAuthority(user);
        return user;
    }

    private void addAuthority(final User user) {
        GrantedAuthority gai = new GrantedAuthorityImpl("ROLE_USER");
        user.setAuthorities(Collections.singletonList(gai));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(final Integer id) {
        return this.getUserDao().getUser(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(String username) {
        return this.getUserDao().getUser(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(final String username) {
        return this.getUserDao().userExists(username);
    }

    @Override
    @Transactional
    public User addUser(final String username, final String password) {
        if (this.getUserDao().userExists(username)) {
            return null;
        }
        return this.getUserDao().addUser(username, password);
    }

    @Override
    @Transactional
    public void followUser(final User user, final User userToFollow) {
        if (!this.getUserDao().isFollowedBy(user, userToFollow)) {
            this.getUserDao().followUser(user, userToFollow);
        }
    }

    @Override
    @Transactional
    public void refreshFollowersFor(Integer ownerId) {
        User user = this.getUserDao().getUser(ownerId);
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
