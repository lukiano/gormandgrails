package com.lucho.security;

import java.util.Collections;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lucho.domain.User;
import com.lucho.repository.UserRepository;

/**
 * {@link UserDetailsService} implementation that allows obtaining users from
 * the repository.
 * @author Luciano.Leggieri
 */
@Service
public final class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * User repository implementation.
     */
    private final UserRepository userRepository;

    /**
     * Class constructor.
     * @param userRepo user repository
     */
    @Inject
    public UserDetailsServiceImpl(final UserRepository userRepo) {
        this.userRepository = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username
                    + " not found.");
        }
        GrantedAuthority gai = new SimpleGrantedAuthority("ROLE_USER");
        user.setAuthorities(Collections.singletonList(gai));
        return user;
    }

}
