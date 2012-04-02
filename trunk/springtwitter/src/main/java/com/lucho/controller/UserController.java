package com.lucho.controller;

import com.lucho.domain.User;
import com.lucho.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.security.Principal;

@Controller
final class UserController {

    private final UserService userService;

    @Inject
    public UserController(final UserService userService) {
        this.userService = userService;
    }

	@Secured({"ROLE_USER"})
	@RequestMapping(value = "/t/follow", method = RequestMethod.POST)
    public void followUser(final Principal principal, @RequestParam(value = "name") final String username) {
        User user = Helper.getUser(principal);
        User userToFollow = this.userService.getUser(username);
        this.userService.followUser(user, userToFollow);
    }

	@Secured ({"ROLE_USER"})
	@RequestMapping(value = "/t/shouldrefresh", method = RequestMethod.GET)
    @ResponseBody
    public Boolean shouldRefresh(final Principal principal) {
        User user = Helper.getUser(principal);
        return this.userService.shouldRefresh(user);
    }

}
