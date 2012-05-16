package com.lucho.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import org.atmosphere.cpr.AtmosphereResource;
import org.codehaus.jackson.map.ObjectMapper;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.repository.TweetRepository;

/**
 * Sends updated tweets to the websocket.
 * @author Luciano.Leggieri
 */
public final class WebsocketStructure {

    /**
     * Object to JSON mapper.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Repository to get and send the latest tweets.
     */
    private final TweetRepository tweetRepository;

    /**
     * Logged user awaiting for broadcaster responses.
     */
    private final User user;

    /**
     * Websocket resource.
     */
    private final AtmosphereResource resource;

    /**
     * Timestamp of the last update sent.
     */
    private long lastUpdate;

    /**
     * Class constructor.
     * @param aTweetRepository tweet repository.
     * @param anUser owner of the broadcasters.
     * @param aResource websocket resource.
     */
    public WebsocketStructure(
            final TweetRepository aTweetRepository,
            final User anUser, final AtmosphereResource aResource) {
        this.tweetRepository = aTweetRepository;
        this.user = anUser;
        this.resource = aResource;
        this.refresh();
    }

    /**
     * Update broadcasters.
     */
    public void sendNewTweets() {
        resource.getBroadcaster().broadcast(new Message());
    }

    /**
     * Update timestamp.
     */
    protected void refresh() {
        this.lastUpdate = System.currentTimeMillis();
    }

    /**
     * @author Luciano.Leggieri
     */
    private class Message implements Callable<String> {

        @Override
        public String call() throws IOException {
            List<Tweet> newTweets = tweetRepository
                    .getTweetsForUserIncludingFollows(user, lastUpdate);
            refresh();
            return MAPPER.writeValueAsString(newTweets);
        }

    }

}
