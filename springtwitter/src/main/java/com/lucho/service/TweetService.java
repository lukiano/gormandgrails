package com.lucho.service;

import com.lucho.dao.TweetDao;
import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/30/11
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TweetService {

    private TweetDao tweetDao;


    @Transactional(readOnly = true)
    @Secured("ROLE_USER")
    public List<Tweet> getTweetsForUser(final User user) {
        return this.tweetDao.getTweetsForUser(user);
    }

    @Autowired
    public TweetDao getTweetDao() {
        return this.tweetDao;
    }

    public void setTweetDao(TweetDao tweetDao) {
        this.tweetDao = tweetDao;
    }

    public Tweet newTweet(User user, String tweet) {
        return this.getTweetDao().newTweet(user, tweet);
    }


}
