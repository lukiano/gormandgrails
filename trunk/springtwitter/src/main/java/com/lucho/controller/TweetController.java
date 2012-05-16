package com.lucho.controller;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Meteor;
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
import com.lucho.service.TweetService;
import com.lucho.service.UserService;

/**
 * MVC Controller for Tweet operations.
 * @author Luciano.Leggieri
 */
@Controller
public final class TweetController {

    /**
     * TweetRepository implementation.
     */
    private final TweetRepository tweetRepository;

    /**
     * TweetService implementation.
     */
    private final TweetService tweetService;

    /**
     * UserService implementation.
     */
    private final UserService userService;

    /**
     * Only Class Constructor.
     * @param tr TweetRepository implementation.
     * @param ts TweetService implementation.
     * @param us UserService implementation.
     */
    @Inject
    public TweetController(final TweetRepository tr,
            final TweetService ts,
            final UserService us) {
        this.tweetRepository = tr;
        this.tweetService = ts;
        this.userService = us;
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
        return this.tweetService.newTweet(user, text);
    }

    /**
     * Shows a friendly message instead of the exception stack trace.
     * @param pe exception.
     * @return the exception message.
     */
    @ExceptionHandler(PersistenceException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlePersistenceException(final PersistenceException pe) {
        String returnMessage;
        if (pe.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) pe
                    .getCause();
            ConstraintViolation<?> cv = cve.getConstraintViolations()
                    .iterator().next();
            returnMessage = cv.getMessage();
        } else {
            returnMessage = pe.getLocalizedMessage();
        }
        return returnMessage;
    }

    /**
     * Shows a friendly message instead of the exception stack trace.
     * @param de exception.
     * @return the exception message.
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDataAccessException(final DataAccessException de) {
        return de.getLocalizedMessage();
    }

    /**
     * Shows a friendly message instead of the exception stack trace.
     * @param te exception.
     * @return the exception message.
     */
    @ExceptionHandler(TransactionException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTransactionException(final TransactionException te) {
        String returnMessage;
        if (te.getMostSpecificCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) te
                    .getMostSpecificCause();
            ConstraintViolation<?> cv = cve.getConstraintViolations()
                    .iterator().next();
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
            @RequestParam(value = "text") final String textToSearch) {
        List<Tweet> tweetList = this.tweetRepository.searchTweets(textToSearch,
                user);
        return tweetList;
    }

    /**
     * Gets the User's tweet line.
     * @param user logged in User.
     * @param millis number of milliseconds that represent a Date. The method
     *            will return only tweets greater than this date, unless the
     *            value is null.
     * @return a lists with the User's tweets and those of the ones it follows.
     */
    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/t/get", method = RequestMethod.GET)
    @ResponseBody
    public List<Tweet> getTweets(@Principal final User user,
            @RequestParam(value = "from", required = false) final Long millis) {
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

    /**
     * @param user logged in User.
     * @param event Atmosphere's resource.
     */
   @RequestMapping(value = "/t/websockets", method = RequestMethod.GET)
   @ResponseBody
   public void websockets(@Principal final User user,
           final AtmosphereResource event) {
       event.suspend();
       this.userService.registerWebsocket(user, event);
   }

}
