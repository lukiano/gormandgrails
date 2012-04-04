package com.lucho.service.impl;

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
import java.util.Collections;
import java.util.List;

@Service("userService")
public final class UserServiceImpl implements UserDetailsService, UserService {

    private final ConcurrentHashSet<Integer> usersToBeRefreshed
            = new ConcurrentHashSet<Integer>();

    private final UserRepository userRepository;

    @Inject
    public UserServiceImpl(final UserRepository anUserRepository) {
        this.userRepository = anUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        User user = this.getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException(
                    "User " + username + " not found.");
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
        return this.userRepository.userExists(username);
    }

    @Override
    @Transactional(readOnly = false)
    public User addUser(final String username, final String password) {
        return this.userRepository.addUser(username, password);
    }

    @Override
    @Transactional
    public void followUser(final User user, final User userToFollow) {
        this.userRepository.followUser(user, userToFollow);
    }

    private boolean notFollowedBy(final User user, final User userToFollow) {
        return this.userRepository.notFollowedBy(user, userToFollow);
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
