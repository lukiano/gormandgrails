package com.lucho.controller;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.repository.TweetRepository;

/**
 * MVC Controller for Tweet operations.
 *
 * @author Luciano.Leggieri
 */
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
    @RequestMapping(value = "/t/new", method = RequestMethod.POST)
    @ResponseBody
    public Tweet newTweet(@Principal final User user,
                          @RequestParam(value = "tweet") final String text) {
        String language = LocaleContextHolder.getLocale().getLanguage();
        Tweet tweet = new Tweet(user, text, language);
        tweet.save();
        return tweet;
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

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDataAccessException(final DataAccessException de) {
        return de.getLocalizedMessage();
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
     * @param millis number of milliseconds that represent a Date.
     *               The method will return only tweets greater than this date,
     *               unless the value is null.
     * @return a lists with the User's tweets and those
     * of the ones it follows.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/t/get", method = RequestMethod.GET)
    @ResponseBody
    public List<Tweet> getTweets(@Principal final User user,
                                 @RequestParam(value = "from", required = false)
                                 final Long millis) {
        return user.getTweetsIncludingFollows(millis);

    }
    
    /**
     * Re indexes Tweets.
     * @return true when re indexing completes.
     */
    @RequestMapping(value = "/reindex", method = RequestMethod.GET)
    @ResponseBody
    public Boolean reindex() {
    	this.tweetRepository.reindex();
    	return true;
    }

}
