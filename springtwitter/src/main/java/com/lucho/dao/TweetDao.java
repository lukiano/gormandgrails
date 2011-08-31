package com.lucho.dao;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/30/11
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class TweetDao {

    private SessionFactory sessionFactory;

    public List<Tweet> getTweetsForUser(final User user) {
        return null;
    }

    public List<Tweet> getTweetsForUserIncludingFollows(final User user) {
        return null;
    }

    public Tweet newTweet(User user, String tweet) {
        return null;
    }

    @Autowired
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
