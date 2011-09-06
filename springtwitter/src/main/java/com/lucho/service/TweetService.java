package com.lucho.service;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;

import java.util.List;

public interface TweetService {

    List<Tweet> getTweetsForUser(User user);

    Tweet newTweet(User user, String tweet);

    List<Tweet> searchTweets(User user, String textToSearch);
}
