package com.lucho.service;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 9/1/11
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TweetService {
    @Transactional(readOnly = true)
    @Secured("ROLE_USER")
    List<Tweet> getTweetsForUser(User user);

    @Transactional
    @Secured("ROLE_USER")
    Tweet newTweet(User user, String tweet);

    @Transactional(readOnly = true)
    @Secured("ROLE_USER")
    List<Tweet> searchTweets(String textToSearch);
}
