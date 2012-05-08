package com.lucho.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.lucho.domain.QUser;
import com.lucho.domain.User;

/**
 * Default {@link UserRepository} implementation.
 *
 * @author Luciano.Leggieri
 */
@Transactional(readOnly = true)
public class UserRepositoryImpl extends QueryDslRepositorySupport
        implements UserRepositoryCustom {

    /**
     * queries if a user is not being followed by another user.
     * @param user user.
     * @param userToFollow user to follow.
     * @return true if userToFollow is not being followed by user.
     */
    private boolean notFollowedBy(final User user, final User userToFollow) {
        QUser quser = QUser.user;
        QUser followedBy = new QUser(quser.followedBy.getMetadata());
        return this.from(quser).join(quser.followedBy, followedBy)
                .where(
                        quser.id.eq(user.getId())
                        .and(followedBy.id.eq(userToFollow.getId()))
                ).notExists();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public final boolean followUser(final User user, final User userToFollow) {
        boolean nowFollows = false;
        if (this.notFollowedBy(user, userToFollow)) {
            User mergedUser = this.getEntityManager().merge(user);
            User mergedUserToFollow =
                    this.getEntityManager().merge(userToFollow);
            Set<User> followedBy = mergedUserToFollow.getFollowedBy();
            followedBy.add(mergedUser);
            this.getEntityManager().merge(mergedUserToFollow);
            nowFollows = true;
        }
        return nowFollows;
    }

}
