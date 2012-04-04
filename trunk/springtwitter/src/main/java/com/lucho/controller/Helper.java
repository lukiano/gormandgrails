package com.lucho.controller;

import com.lucho.domain.User;
import org.springframework.security.core.Authentication;

import java.security.Principal;

/**
 * Helper class to get the User from Spring Security system.
 * @author Luciano.Leggieri
 */
final class Helper {

    /**
     * Private constructor to avoid instantiation of this class.
     */
    private Helper() {
    }

    /**
     * Obtains the logged in user from the security principal.
     * @param principal the logged in entity.
     * @return User domain object that matches the logged in entity.
     */
    public static User getUser(final Principal principal) {
        Authentication authentication = (Authentication) principal;
        User user = null;
        if (authentication != null) {
            user = (User) authentication.getPrincipal();
        }
        return user;
    }
}
