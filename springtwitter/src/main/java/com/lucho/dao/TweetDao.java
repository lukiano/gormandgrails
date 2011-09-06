package com.lucho.dao;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;

import java.util.List;

public interface TweetDao {

    List<Tweet> getTweetsForUser(User user);

    List<Tweet> getTweetsForUserIncludingFollows(User user);

    Tweet newTweet(User user, String text);

    List<Tweet> searchTweets(String textToSearch);
}
