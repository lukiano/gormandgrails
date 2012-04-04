package com.lucho.repository;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luciano
 * Date: 4/3/12
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TweetRepositoryCustom {

    List<Tweet> getTweetsForUserIncludingFollows(User user);

    Tweet newTweet(User user, String text, String language);

    List<Tweet> searchTweets(String textToSearch);

}
