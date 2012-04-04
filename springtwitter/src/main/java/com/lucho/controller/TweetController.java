package com.lucho.controller;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.service.TweetService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.security.Principal;
import java.util.List;

/**
 * @author Luciano.Leggieri
 */
@Controller
public final class TweetController {

    private final TweetService tweetService;

    @Inject
    public TweetController(final TweetService tweetService) {
        this.tweetService = tweetService;
    }

	@Secured ({"ROLE_USER"})
	@RequestMapping(value = "/t/new", method = RequestMethod.POST)
    @ResponseBody
	public Tweet newTweet(final Principal principal, final String tweet) {
        User user = Helper.getUser(principal);
        return this.tweetService.newTweet(user, tweet);
    }

	@Secured ({"ROLE_USER"})
	@RequestMapping(value = "/t/search", method = RequestMethod.GET)
    @ResponseBody
    public List<Tweet> searchInTweets(final Principal principal, @RequestParam(value = "text") final String text) {
        User user = Helper.getUser(principal);
        return this.tweetService.searchTweets(user, text);
    }

	@Secured ({"ROLE_USER"})
	@RequestMapping(value = "/t/get", method = RequestMethod.GET)
    @ResponseBody
    public List<Tweet> getTweets(final Principal principal) {
        User user = Helper.getUser(principal);
        return this.tweetService.getTweetsForUser(user);
    }

}
