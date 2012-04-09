package com.lucho.service.impl;

import com.lucho.domain.User;
import com.lucho.repository.UserRepository;
import com.lucho.service.UserService;
import org.infinispan.api.BasicCacheContainer;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@Service("userService")
@DependsOn("infinispanCacheManager")
@Transactional
public final class UserServiceImpl implements UserDetailsService, UserService {

    private final ConcurrentMap<Integer, Serializable> usersToBeRefreshed;

    private final UserRepository userRepository;

    @Inject
    public UserServiceImpl(final UserRepository anUserRepository,
                           final BasicCacheContainer ecm) {
        this.userRepository = anUserRepository;
        this.usersToBeRefreshed = ecm.getCache("refresher");
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(
                    "User " + username + " not found.");
        }
        GrantedAuthority gai = new SimpleGrantedAuthority("ROLE_USER");
        user.setAuthorities(Collections.singletonList(gai));
        return user;
    }

    @Override
    public void refreshFollowersFor(final Integer ownerId) {
        User user = this.userRepository.findOne(ownerId);
        List<User> followedByList = user.getFollowedBy();
        for (User follower : followedByList) {
            usersToBeRefreshed.put(follower.getId(), Boolean.TRUE);
        }
    }

    @Override
    public boolean shouldRefresh(final User user) {
        return usersToBeRefreshed.remove(user.getId()) != null;
    }

}
