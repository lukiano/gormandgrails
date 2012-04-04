package com.lucho.service.impl;

import com.lucho.dao.TweetDao;
import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.repository.TweetRepository;
import com.lucho.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.integration.annotation.Publisher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public final class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;

    @Autowired
    public TweetServiceImpl(final TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    @Transactional(readOnly = true)
    @Secured("ROLE_USER")
    public List<Tweet> getTweetsForUser(final User user) {
        List<Tweet> tweetList = this.tweetRepository.getTweetsForUserIncludingFollows(user);
        for (Tweet tweet : tweetList) {
            tweet.getOwner().setBeingFollowed(true);
        }
        return tweetList;
    }


    @Override
    @Transactional
    @Secured("ROLE_USER")
	@Publisher(channel = "newTweetNotificationChannel")
    public Tweet newTweet(final User user, final String text) {
		String language = LocaleContextHolder.getLocale().getLanguage();
        Tweet newTweet = this.tweetRepository.newTweet(user, text, language);
        newTweet.getOwner().setBeingFollowed(true);
        return newTweet;
    }

    @Override
    @Transactional(readOnly = true)
    @Secured("ROLE_USER")
    public List<Tweet> searchTweets(final User user, final String textToSearch) {
        List<Tweet> tweetList =  this.tweetRepository.searchTweets(textToSearch);
        for (Tweet tweet : tweetList) {
            boolean followed = tweet.getOwner().getFollowedBy().contains(user);
            tweet.getOwner().setBeingFollowed(followed);
        }
        return tweetList;
    }

}
