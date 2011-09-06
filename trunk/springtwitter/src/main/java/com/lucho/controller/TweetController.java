package com.lucho.controller;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.service.TweetService;
import com.lucho.service.TwitterMessageListener;
import com.lucho.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class TweetController {

    private TweetService tweetService;

    public TweetService getTweetService() {
        return tweetService;
    }

    @Autowired
    public void setTweetService(final TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @RequestMapping(value = "/t/new", method = RequestMethod.POST)
    public
    @ResponseBody
    Tweet newTweet(final Principal principal, final String tweet) {
        User user = Helper.getUser(principal);
        return this.getTweetService().newTweet(user, tweet);
    }

    @RequestMapping(value = "/t/search", method = RequestMethod.GET)
    public List<Tweet> searchInTweets(final Principal principal, final @RequestParam(value = "text") String text) {
        User user = Helper.getUser(principal);
        return this.getTweetService().searchTweets(user, text);
    }

    @RequestMapping(value = "/t/get", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Tweet> getTweets(final Principal principal) {
        User user = Helper.getUser(principal);
        return this.getTweetService().getTweetsForUser(user);
    }

}
