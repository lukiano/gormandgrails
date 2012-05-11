package com.lucho.service.impl;

import javax.inject.Inject;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.integration.Message;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.service.TweetService;

/**
 * {@link TweetService} default implementation.
 *
 * @author Luciano.Leggieri
 */
@Transactional
@Service
public final class TweetServiceImpl implements TweetService {

    /**
     * Spring Integration messaging system.
     */
    private final MessagingTemplate messagingTemplate;

    /**
     * Default class constructor.
     * @param mt messaging system.
     */
    @Inject
    public TweetServiceImpl(final MessagingTemplate mt) {
        this.messagingTemplate = mt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tweet newTweet(final User user, final String text) {
        String language = LocaleContextHolder.getLocale().getLanguage();
        Tweet tweet = new Tweet(user, text, language);
        tweet.save();
        Message<Tweet> message = MessageBuilder.withPayload(tweet).build();
        this.messagingTemplate.send(message);
        return tweet;
    }

}
