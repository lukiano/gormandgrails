package com.lucho.controller;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.repository.TweetRepository;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.Publisher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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
     * @param tweet the text that will go in the new tweet.
     * @return a new Tweet with the desired text.
     */
    @Secured({ "ROLE_USER" })
    @Publisher(channel = "newTweetNotificationChannel")
    @RequestMapping(value = "/t/new", method = RequestMethod.POST)
    @ResponseBody
    public Tweet newTweet(@Principal final User user,
                          @RequestParam(value = "tweet") final String tweet) {
        String language = LocaleContextHolder.getLocale().getLanguage();
        return this.tweetRepository.newTweet(user, tweet, language);
        /*
        Message<Tweet> message = MessageBuilder.withPayload(newTweet).build();
        this.messagingTemplate.send("newTweetNotificationChannel", message);
        return newTweet;
        */
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlePersistenceException(final PersistenceException pe) {
        String returnMessage;
        if (pe.getCause()
                instanceof ConstraintViolationException) {
            ConstraintViolationException cve =
                    (ConstraintViolationException) pe.getCause();
            ConstraintViolation<?> cv =
                    cve.getConstraintViolations().iterator().next();
            returnMessage = cv.getMessage();
        } else {
            returnMessage = pe.getLocalizedMessage();
        }
        return returnMessage;
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTransactionException(final TransactionException te) {
        String returnMessage;
        if (te.getMostSpecificCause()
                instanceof ConstraintViolationException) {
            ConstraintViolationException cve =
                    (ConstraintViolationException) te.getMostSpecificCause();
            ConstraintViolation<?> cv =
                    cve.getConstraintViolations().iterator().next();
            returnMessage = cv.getMessage();
        } else {
            returnMessage = te.getLocalizedMessage();
        }
        return returnMessage;
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
