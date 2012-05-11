package com.lucho.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Configurable;

import com.lucho.domain.Tweet;
import com.lucho.domain.User;
import com.lucho.repository.TweetRepository;

@Configurable
public class BroadcastStructure {

    @Inject
    private TweetRepository tweetRepository;

    private final User user;

    final ObjectMapper mapper = new ObjectMapper();

    private long lastUpdate;

    private final ArrayList<Broadcaster> casters = new ArrayList<Broadcaster>();

    public BroadcastStructure(final User anUser) {
        this.lastUpdate = System.currentTimeMillis();
        this.user = anUser;
    }

    public void sendNewTweets() {
        for (Broadcaster broadcaster : casters) {
            broadcaster.broadcast(new Message());
        }
    }

    public void addBroadcaster(final Broadcaster broadcaster) {
        this.casters.add(broadcaster);
    }

    private class Message implements Callable<String> {

        @Override
        public String call() throws Exception {
            List<Tweet> newTweets = tweetRepository
                    .getTweetsForUserIncludingFollows(user, lastUpdate);
            return mapper.writeValueAsString(newTweets);
        }

    }

}
