package com.lucho.repository;

import com.lucho.domain.User;

/**
 * Extension for User Repository.
 *
 * @author Luciano.Leggieri
 */
public interface UserRepositoryCustom {

    /**
     * @param user user who wants to follow someone else's tweets.
     * @param userToFollow the user to follow.
     * @return true if user now follows userToFollow and
     * didn't do it before.
     */
    boolean followUser(User user, User userToFollow);

}

