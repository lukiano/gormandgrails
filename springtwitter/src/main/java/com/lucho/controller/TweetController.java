package com.lucho.controller;

import com.lucho.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/31/11
 * Time: 11:31 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class TweetController {

    @Autowired
    public TweetService getTweetService() {
        return tweetService;
    }

    public void setTweetService(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    private TweetService tweetService;

    public void newTweet(Principal principal, @Valid String tweet) {

    }

    public void searchInTweets(Principal principal, String text) {

    }

    public void getTweets(Principal principal) {

    }
}
