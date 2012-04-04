package com.lucho.dao.impl;

import com.lucho.dao.UserDao;
import com.lucho.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

final class UserDaoImpl implements UserDao {

	@PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUser(final Integer id) {
        return this.entityManager.find(User.class, id);
    }

    @Override
    public User getUser(final String username) {
        Query query = this.entityManager.createQuery("select u from User u where u.username = :name");
        query.setParameter("name", username);
        return (User) query.getSingleResult();
    }

    @Override
    public boolean userExists(final String username) {
        Query query = this.entityManager.createQuery("select count(user) from User user where user.username = :name");
        query.setParameter("name", username);
        Long count = (Long) query.getSingleResult();
        return count.intValue() == 1;
    }

    @Override
    public User addUser(final String username, final String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        this.entityManager.persist(user);
        List<User> followedBy = user.getFollowedBy();
        if (followedBy == null) {
            followedBy = new ArrayList<User>();
            followedBy.add(user);
            user.setFollowedBy(followedBy);
        } else {
            followedBy.add(user);
        }
        //user = this.entityManager.merge(user);
		//this.entityManager.flush();
        return user;
    }

    @Override
    public boolean notFollowedBy(final User user, final User userToFollow) {
        Query query = this.entityManager.createQuery("select count(user) from User user inner join user.followedBy followed where user.id = :userToFollowId and followed.id = :userId");
        query.setParameter("userToFollowId", userToFollow.getId());
        query.setParameter("userId", user.getId());
        Long count = (Long) query.getSingleResult();
        return count.intValue() == 0;
    }

    @Override
    public void followUser(final User user, final User userToFollow) {
        User mergedUser = this.entityManager.merge(user);
        User mergedUserToFollow = this.entityManager.merge(userToFollow);
        List<User> followedBy = mergedUserToFollow.getFollowedBy();
        followedBy.add(mergedUser);
        this.entityManager.merge(mergedUserToFollow);
    }
}
