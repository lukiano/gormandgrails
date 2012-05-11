package com.lucho.service.impl;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;

import org.atmosphere.cpr.Broadcaster;
import org.infinispan.api.BasicCacheContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucho.domain.User;
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
     * Map that holds pair id-broadcaster for websockets.
     */
    private final ConcurrentMap<Integer, BroadcastStructure> broadcasters;

    /**
     * User repository implementation.
     */
    private final UserRepository userRepository;

    /**
     * Name of the Transactional Cache used as a {@link ConcurrentMap}.
     */
    public static final String REFRESHER_CACHE_NAME = "refresher";

    /**
     * Name of the Transactional Cache used as a {@link ConcurrentMap}.
     */
    public static final String BROADCASTER_CACHE_NAME = "broadcaster";

    /**
     * Logger.
     */
    private static final Logger LOG =
            LoggerFactory.getLogger(UserService.class);

    /**
     * Class constructor.
     *
     * @param anUserRepository injects user repository implementation.
     * @param ecm              injects container used to get the Cache Map.
     */
    @Inject
    public UserServiceImpl(final UserRepository anUserRepository,
                           final BasicCacheContainer ecm) {
        this.userRepository = anUserRepository;
        this.usersToBeRefreshed = ecm.getCache(REFRESHER_CACHE_NAME);
        this.broadcasters = ecm.getCache(BROADCASTER_CACHE_NAME);
    }

    @Override
    public void refreshFollowersFor(final Integer ownerId) {
        User user = this.userRepository.findOne(ownerId);
        LOG.info("Refreshing followers for user " + user);
        for (User follower : user.getFollowedBy()) {
            usersToBeRefreshed.put(follower.getId(), Boolean.TRUE);
            if (broadcasters.containsKey(ownerId)) {
                broadcasters.get(ownerId).sendNewTweets();
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
    public void registerBroadcaster(final User user,
            final Broadcaster broadcaster) {
        Integer id = user.getId();
        if (broadcasters.containsKey(id)) {
            broadcasters.get(id).addBroadcaster(broadcaster);
        } else {
            BroadcastStructure broadcastStructure =
                    new BroadcastStructure(user);
            broadcasters.put(id, broadcastStructure);
            broadcastStructure.addBroadcaster(broadcaster);
        }
    }
}
