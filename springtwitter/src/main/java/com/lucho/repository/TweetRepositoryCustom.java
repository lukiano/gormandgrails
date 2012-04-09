package com.lucho.repository;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luciano
 * Date: 4/3/12
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public interface TweetRepositoryCustom {

    @Transactional(readOnly = true)
    List<Tweet> getTweetsForUserIncludingFollows(User user);

    @Transactional
    Tweet newTweet(User user, String text, String language);

    @Transactional(readOnly = true)
    List<Tweet> searchTweets(String textToSearch);

}
