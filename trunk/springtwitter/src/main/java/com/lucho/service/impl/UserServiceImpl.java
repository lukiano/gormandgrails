package com.lucho.service.impl;

import com.lucho.domain.QUser;
import com.lucho.domain.User;
import com.lucho.repository.UserRepository;
import com.lucho.service.UserService;
import org.infinispan.util.concurrent.ConcurrentHashSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("userService")
public final class UserServiceImpl implements UserDetailsService, UserService {

    private final ConcurrentHashSet<Integer> usersToBeRefreshed = new ConcurrentHashSet<Integer>();

    private UserRepository userRepository;

    @Inject
    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return this.userRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(final String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(final String username) {
        return this.userRepository.count(QUser.user.username.eq(username)) > 0;
    }

    @Override
    @Transactional(readOnly = false)
    public User addUser(final String username, final String password) {
        User newUser = null;
        if (!this.userExists(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user = this.userRepository.saveAndFlush(user);
            List<User> followedBy = user.getFollowedBy();
            if (followedBy == null) {
                followedBy = new ArrayList<>();
                followedBy.add(user);
                user.setFollowedBy(followedBy);
            } else {
                followedBy.add(user);
            }
            newUser = this.userRepository.save(user);
        }
        return newUser;
    }

    @Override
    @Transactional
    public void followUser(final User user, final User userToFollow) {
        /*
        if (this.userDao.notFollowedBy(user, userToFollow)) {
            this.userDao.followUser(user, userToFollow);
        }
        */
    }

    @Override
    @Transactional
    public void refreshFollowersFor(final Integer ownerId) {
        User user = this.userRepository.findOne(ownerId);
        List<User> followedByList = user.getFollowedBy();
        for (User follower : followedByList) {
            usersToBeRefreshed.add(follower.getId());
        }
    }

    @Override
    public boolean shouldRefresh(final User user) {
        return usersToBeRefreshed.remove(user.getId());
    }

}
