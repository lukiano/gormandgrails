package com.lucho.dao;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 9/1/11
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TweetDao {
    List<Tweet> getTweetsForUser(User user);

    List<Tweet> getTweetsForUserIncludingFollows(User user);

    Tweet newTweet(User user, String text);

    List<Tweet> searchTweets(String textToSearch);
}
