package com.lucho.service.impl;

import com.lucho.dao.TweetDao;
import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.List;

@Service
public class TweetServiceImpl implements TweetService {

    private TweetDao tweetDao;

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    @Autowired
    public void setJmsTemplate(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    private JmsTemplate jmsTemplate;

    @Override
    @Transactional(readOnly = true)
    @Secured("ROLE_USER")
    public List<Tweet> getTweetsForUser(final User user) {
        List<Tweet> tweetList = this.tweetDao.getTweetsForUserIncludingFollows(user);
        for (Tweet tweet : tweetList) {
            tweet.getOwner().setBeingFollowed(true);
        }
        return tweetList;
    }

    public TweetDao getTweetDao() {
        return this.tweetDao;
    }

    @Autowired
    public void setTweetDao(final TweetDao tweetDao) {
        this.tweetDao = tweetDao;
    }

    @Override
    @Transactional
    @Secured("ROLE_USER")
    public Tweet newTweet(final User user, final String text) {
        Tweet newTweet = this.getTweetDao().newTweet(user, text);
        //newTweet.getOwner().setBeingFollowed(true);
        //this.sendMessage(newTweet);
        return newTweet;
    }

    private void sendMessage(final Tweet newTweet) {
        MessageCreator messageCreator = new TweetMessageCreator(newTweet);
        this.getJmsTemplate().send(messageCreator);
    }

    @Override
    @Transactional(readOnly = true)
    @Secured("ROLE_USER")
    public List<Tweet> searchTweets(final User user, final String textToSearch) {
        List<Tweet> tweetList =  this.getTweetDao().searchTweets(textToSearch);
        for (Tweet tweet : tweetList) {
            boolean followed = tweet.getOwner().getFollowedBy().contains(user);
            tweet.getOwner().setBeingFollowed(followed);
        }
        return tweetList;
    }

    private static class TweetMessageCreator implements MessageCreator {

        private Tweet tweet;

        public TweetMessageCreator(final Tweet tweet) {
            this.tweet = tweet;
        }

        @Override
        public Message createMessage(Session session) throws JMSException {
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setInt("owner", this.tweet.getOwner().getId());
            mapMessage.setString("tweet", this.tweet.getTweet());
            return mapMessage;
        }

    }

}
