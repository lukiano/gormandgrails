package com.lucho.controller;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.service.TweetService;
import com.lucho.service.TwitterMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/31/11
 * Time: 11:31 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class TweetController {

    public TwitterMessageListener getTwitterMessageListener() {
        return twitterMessageListener;
    }

    @Autowired
    public void setTwitterMessageListener(final TwitterMessageListener twitterMessageListener) {
        this.twitterMessageListener = twitterMessageListener;
    }

    private TwitterMessageListener twitterMessageListener;

    public TweetService getTweetService() {
        return tweetService;
    }

    @Autowired
    public void setTweetService(final TweetService tweetService) {
        this.tweetService = tweetService;
    }

    private TweetService tweetService;

    @RequestMapping(value = "/t/new", method = RequestMethod.POST)
    public void newTweet(final Principal principal, final String tweet) {
        User user = this.getUser(principal);
        this.getTweetService().newTweet(user, tweet);
    }

    @RequestMapping(value = "/t/search", method = RequestMethod.GET)
    public List<Tweet> searchInTweets(final Principal principal, final String text) {
        User user = this.getUser(principal);
        return this.getTweetService().searchTweets(text);
    }

    @RequestMapping(value = "/t/get", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Tweet> getTweets(final Principal principal) {
        User user = this.getUser(principal);
        return this.getTweetService().getTweetsForUser(user);
    }

    private User getUser(final Principal principal) {
        Authentication authentication = (Authentication) principal;
        return (User) authentication.getPrincipal();
    }

    @RequestMapping(value = "/t/shouldrefresh", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean shouldrefresh(final Principal principal) {
        User user = this.getUser(principal);
        return this.getTwitterMessageListener().getUsersToBeRefreshed().contains(user.getId());
    }
}
