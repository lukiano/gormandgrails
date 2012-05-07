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
     *
     * @param user
     * @param millis
     * @return
     */
    List<Tweet> getTweetsForUserIncludingFollows(User user, Long millis);

    List<Tweet> searchTweets(String textToSearch, User user);

    void saveAndLetOthersKnow(Tweet tweet);
    
    void reindex();
}
