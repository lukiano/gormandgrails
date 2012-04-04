package com.lucho.service;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;

import java.util.List;

/**
 * Service for tweets Management.
 *
 * @see Tweet
 */
public interface TweetService {

    /**
     * Get the tweets for a user the the users it follows.
     * @param user the user whose tweet are desired.
     * @return a lists of tweets.
     */
    List<Tweet> getTweetsForUser(User user);

    /**
     * Creates a new tweet for a user.
     * @param user the owner of the new tweet.
     * @param tweetText the text that goes in the tweet.
     * @return a brand new Tweet object, already persisted.
     */
    Tweet newTweet(User user, String tweetText);

    /**
     * Search all tweets a user can see that have a particular text.
     * @param user the user that is doing the search.
     * @param textToSearch the text to search.
     * @return a list of tweets with the results.
     */
    List<Tweet> searchTweets(User user, String textToSearch);
}
