package com.lucho.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListener;
import org.infinispan.api.BasicCacheContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Supplier;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.lucho.domain.User;
import com.lucho.repository.TweetRepository;
import com.lucho.repository.UserRepository;
import com.lucho.service.UserService;

/**
 * {@link UserService} default implementation.
 *
 * @author Luciano.Leggieri
 */
@Service("userService")
@DependsOn("infinispanCacheManager")
@Transactional
public final class UserServiceImpl implements UserService {

    /**
     * Map that holds the ids of the Users whose tweet line needs
     * to be refreshed.
     */
    private final ConcurrentMap<Integer, Serializable> usersToBeRefreshed;

    /**
     * Map that holds pair user id for websockets.
     */
    private final ListMultimap<Integer, WebsocketStructure> websockets;

    /**
     * User repository implementation.
     */
    private final UserRepository userRepository;

    /**
     * Name of the Transactional Cache used as a {@link ConcurrentMap}.
     */
    public static final String REFRESHER_CACHE_NAME = "refresher";

    /**
     * Repository to get and send the latest tweets.
     */
    private final TweetRepository tweetRepository;


    /**
     * Logger.
     */
    private static final Logger LOG =
            LoggerFactory.getLogger(UserService.class);

    /**
     * Class constructor.
     *
     * @param anUserRepository injects user repository implementation.
     * @param aTweetRepository injects tweet repository implementation.
     * @param ecm              injects container used to get the Cache Map.
     */
    @Inject
    public UserServiceImpl(final UserRepository anUserRepository,
                           final TweetRepository aTweetRepository,
                           final BasicCacheContainer ecm) {
        this.userRepository = anUserRepository;
        this.tweetRepository = aTweetRepository;
        this.usersToBeRefreshed = ecm.getCache(REFRESHER_CACHE_NAME);
        Supplier<List<WebsocketStructure>> supplier =
                new Supplier<List<WebsocketStructure>>() {

            @Override
            public List<WebsocketStructure> get() {
                return Lists.newArrayList();
            }
        };
        Map<Integer, Collection<WebsocketStructure>> map = Maps.newHashMap();
        this.websockets = Multimaps.synchronizedListMultimap(
                Multimaps.newListMultimap(map, supplier));
    }

    @Override
    public void refreshFollowersFor(final Integer ownerId) {
        User user = this.userRepository.findOne(ownerId);
        LOG.info("Refreshing followers for user " + user);
        for (User follower : user.getFollowedBy()) {
            usersToBeRefreshed.put(follower.getId(), Boolean.TRUE);
            synchronized (websockets) {
                if (websockets.containsKey(ownerId)) {
                    for (WebsocketStructure wss : websockets.get(ownerId)) {
                        wss.sendNewTweets();
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldRefresh(final User user) {
        boolean shouldRefresh = usersToBeRefreshed.remove(user.getId()) != null;
        if (shouldRefresh) {
            LOG.debug("User " + user + " should be refreshed.");
        } else {
            LOG.debug("User " + user + " should NOT be refreshed.");
        }
        return shouldRefresh;
    }

    @Override
    public void registerWebsocket(final User user,
            final AtmosphereResource resource) {
        final Integer id = user.getId();
        WebsocketStructure wss = new WebsocketStructure(
                this.tweetRepository, user, resource);
        resource.addEventListener(new AtmosphereResourceEventListener() {

            @Override
            public void onThrowable(final AtmosphereResourceEvent event) {
                websockets.remove(id, resource);
            }

            @Override
            public void onSuspend(final AtmosphereResourceEvent event) {
            }

            @Override
            public void onResume(final AtmosphereResourceEvent event) {
            }

            @Override
            public void onDisconnect(final AtmosphereResourceEvent event) {
                websockets.remove(id, resource);
            }

            @Override
            public void onBroadcast(final AtmosphereResourceEvent event) {
            }
        });
        websockets.put(id,  wss);
    }
}
