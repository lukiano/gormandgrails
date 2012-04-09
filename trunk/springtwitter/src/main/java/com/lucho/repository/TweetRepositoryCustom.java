package com.lucho.repository;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Extension for Tweet Repository.
 *
 * @author Luciano.Leggieri
 */
@Transactional
public interface TweetRepositoryCustom {

    /**
     *
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    List<Tweet> getTweetsForUserIncludingFollows(User user);

    @Transactional
    Tweet newTweet(User user, String text, String language);

    @Transactional(readOnly = true)
    List<Tweet> searchTweets(String textToSearch);

}
