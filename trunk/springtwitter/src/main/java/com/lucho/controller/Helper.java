package com.lucho.controller;

import com.lucho.domain.User;
import org.springframework.security.core.Authentication;

import java.security.Principal;

final class Helper {

    private Helper() {
    }

    public static User getUser(final Principal principal) {
        Authentication authentication = (Authentication) principal;
		if (authentication == null) return null;
        return (User) authentication.getPrincipal();
    }
}
