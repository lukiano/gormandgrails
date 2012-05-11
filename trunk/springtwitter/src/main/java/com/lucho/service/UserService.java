package com.lucho.service;

import org.atmosphere.cpr.Broadcaster;

import com.lucho.domain.User;

/**
 * Service that deals with knowing when a User's tweet line
 * refresh is needed because someone else that the User follows has made
 * new tweets.
 * @author Luciano.Leggieri
 */
public interface UserService {

    /**
     * Tells the system that the followers of this User's id need
     * to refresh their tweet lines.
     * @param ownerId a User id.
     */
    void refreshFollowersFor(Integer ownerId);

    /**
     * Asks if this User should refresh his tweet line.
     * @param user a User.
     * @return true if this User should refresh his tweet line.
     */
    boolean shouldRefresh(User user);
    
    void registerBroadcaster(User user, Broadcaster broadcaster);
}
