package com.lucho.repository;

import com.lucho.domain.QUser;
import com.lucho.domain.User;
import com.lucho.repository.UserRepositoryCustom;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl extends QueryDslRepositorySupport implements UserRepositoryCustom {

    @Override
    public User addUser(String username, String password) {
        User newUser = null;
        if (!this.userExists(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            this.getEntityManager().persist(user);
            List<User> followedBy = user.getFollowedBy();
            if (followedBy == null) {
                followedBy = new ArrayList<>();
                followedBy.add(user);
                user.setFollowedBy(followedBy);
            } else {
                followedBy.add(user);
            }
            newUser = this.getEntityManager().merge(user);
        }
        return newUser;
    }

    public boolean userExists(final String username) {
        QUser quser = QUser.user;
        return this.from(quser).where(quser.username.eq(username)).exists();
    }

    @Override
    public boolean notFollowedBy(final User user, final User userToFollow) {
        QUser quser = QUser.user;
        QUser followedBy = new QUser("followedBy");
        return this.from(quser).join(quser.followedBy, followedBy)
                .where(quser.id.eq(user.getId()).and(followedBy.id.eq(userToFollow.getId()))).notExists();
    }

    @Override
    public void followUser(final User user, final User userToFollow) {
        if (this.notFollowedBy(user, userToFollow)) {
            User mergedUser = this.getEntityManager().merge(user);
            User mergedUserToFollow = this.getEntityManager().merge(userToFollow);
            List<User> followedBy = mergedUserToFollow.getFollowedBy();
            followedBy.add(mergedUser);
            this.getEntityManager().merge(mergedUserToFollow);
        }
    }

}
