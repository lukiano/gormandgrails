package com.lucho.controller;

import com.lucho.domain.User;
import com.lucho.repository.UserRepository;
import com.lucho.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * Controller that provides login related endpoints.
 *
 * @author Luciano.Leggieri
 */
@Controller
public final class UserController {

    /**
     * User service.
     */
    private final UserService userService;
    /**
     * User repository.
     */
    private final UserRepository userRepository;

    /**
     * Class constructor.
     *
     * @param anUserRepository user repository.
     * @param anUserService user service.
     */
    @Inject
    public UserController(final UserRepository anUserRepository,
                          final UserService anUserService) {
        this.userRepository = anUserRepository;
        this.userService = anUserService;
    }

    /**
     * Makes the current user to follow the tweets of the desired user,
     * if it doesn't do that already.
     *
     * @param user         the logged in user.
     * @param userToFollow the user to follow.
     */
    @RequestMapping(value = "/t/follow", method = RequestMethod.POST)
    public void followUser(@Principal final User user,
                           @RequestParam(value = "id")
                           final User userToFollow) {
        this.userRepository.followUser(user, userToFollow);
    }

    /**
     * Checks if the logged in user has new activity in his tweetline.
     * @param user the logged in user.
     * @return true if the logged in user has new activity in his tweetline.
     */
    @RequestMapping(value = "/t/shouldrefresh", method = RequestMethod.GET)
    @ResponseBody
    public Boolean shouldRefresh(@Principal final User user) {
        return this.userService.shouldRefresh(user);
    }

}
