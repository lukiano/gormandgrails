package com.lucho.controller;

import com.lucho.domain.User;
import org.springframework.security.core.Authentication;

import java.security.Principal;

public final class Helper {

    private Helper() {
    }

    public static User getUser(final Principal principal) {
        Authentication authentication = (Authentication) principal;
        return (User) authentication.getPrincipal();
    }
}
