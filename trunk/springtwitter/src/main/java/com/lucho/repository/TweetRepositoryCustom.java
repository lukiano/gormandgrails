package com.lucho.repository;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;

import java.util.List;

/**
 * Extension for Tweet Repository.
 *
 * @author Luciano.Leggieri
 */
public interface TweetRepositoryCustom {

    /**
     * Obtains user's tweetline.
     * @param user User whose tweetline will be obtained.
     * @param millis a date in milliseconds. Only retrieve tweets newer than it.
     * @return a list of tweets that form the user tweetline.
     */
    List<Tweet> getTweetsForUserIncludingFollows(User user, Long millis);

    /**
     * Search and return tweets that contain a specified word or phrase.
     * @param textToSearch the text that will be included in tweets.
     * @param user user who is making the search.
     * @return a list of tweets that contain the desired
     * text and are visible to the user.
     */
    List<Tweet> searchTweets(String textToSearch, User user);

    /**
     * Performs reindexing of the tweet text index repository.
     */
    void reindex();

}
