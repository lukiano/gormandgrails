package com.lucho.controller;

import com.lucho.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller that provides login related endpoints.
 *
 * @author Luciano.Leggieri
 */
@Controller
public final class UserController {

    /**
     * Makes the current user to follow the tweets of the desired user,
     * if it doesn't do that already.
     *
     * @param user         the logged in user.
     * @param userToFollow the user to follow.
     * @return true if user now follows userToFollow and
     * didn't do it before.
     */
    @RequestMapping(value = "/t/follow", method = RequestMethod.POST)
    @ResponseBody
    public Boolean followUser(@Principal final User user,
                              @RequestParam(value = "id")
                              final User userToFollow) {
        return user.followUser(userToFollow);
    }

    /**
     * Checks if the logged in user has new activity in his tweetline.
     *
     * @param user the logged in user.
     * @return true if the logged in user has new activity in his tweetline.
     */
    @RequestMapping(value = "/t/shouldrefresh", method = RequestMethod.GET)
    @ResponseBody
    public Boolean shouldRefresh(@Principal final User user) {
        return user.shouldRefresh();
    }

}
