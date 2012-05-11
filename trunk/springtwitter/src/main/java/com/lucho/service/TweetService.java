package com.lucho.service;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;

/**
 * Service to create a new tweet and notify its followers.
 * @author Luciano.Leggieri
 *
 */
public interface TweetService {

    /**
     * Creates a new tweet and notifies its followers.
     * @param user tweet owner.
     * @param text tweet text.
     * @return a new Tweet.
     */
    Tweet newTweet(User user, String text);

}
