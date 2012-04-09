package com.lucho.controller;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.repository.TweetRepository;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.integration.annotation.Publisher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 * MVC Controller for Tweet operations.
 *
 * @author Luciano.Leggieri
 */
@Transactional
@Controller
public final class TweetController {

    /**
     * TweetRepository implementation.
     */
    private final TweetRepository tweetRepository;

    /**
     * Only Class Constructor.
     *
     * @param tr TweetRepository implementation.
     */
    @Inject
    public TweetController(final TweetRepository tr) {
        this.tweetRepository = tr;
    }

    /**
     * Creates a new tweet.
     * @param user logged in User.
     * @param text the text that will go in the new tweet.
     * @return a new Tweet with the desired text.
     */
    @Secured({ "ROLE_USER" })
    @Publisher(channel = "newTweetNotificationChannel")
    @RequestMapping(value = "/t/new", method = RequestMethod.POST)
    @ResponseBody
    public Tweet newTweet(@Principal final User user, final String text) {
        String language = LocaleContextHolder.getLocale().getLanguage();
        return this.tweetRepository.newTweet(user, text, language);
        /*
        Message<Tweet> message = MessageBuilder.withPayload(newTweet).build();
        this.messagingTemplate.send("newTweetNotificationChannel", message);
        return newTweet;
        */
    }

    /**
     * Finds tweets that contain a certain text.
     * @param user logged in User.
     * @param textToSearch text string to search.
     * @return a list with tweets that have the text in their contents.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/t/search", method = RequestMethod.GET)
    @ResponseBody
    public List<Tweet> searchInTweets(@Principal final User user,
                                      @RequestParam(value = "text")
                                      final String textToSearch) {
        List<Tweet> tweetList = this.tweetRepository.searchTweets(textToSearch);
        for (Tweet tweet : tweetList) {
            boolean followed = tweet.getOwner().getFollowedBy().contains(user);
            tweet.getOwner().setCanFollow(!followed);
        }
        return tweetList;
    }

    /**
     * Gets the User's tweet line.
     * @param user logged in User.
     * @return a lists with the User's tweets and those
     * of the ones it follows.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/t/get", method = RequestMethod.GET)
    @ResponseBody
    public List<Tweet> getTweets(@Principal final User user) {
        return this.tweetRepository.getTweetsForUserIncludingFollows(user);

    }

}
