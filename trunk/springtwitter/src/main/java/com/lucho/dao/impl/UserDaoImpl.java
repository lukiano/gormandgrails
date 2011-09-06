package com.lucho.dao.impl;

import com.lucho.dao.UserDao;
import com.lucho.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private SessionFactory sessionFactory;

    @Override
    public User getUser(final Integer id) {
        Session session = this.getSessionFactory().getCurrentSession();
        return (User) session.get(User.class, id);
    }

    @Override
    public User getUser(final String username) {
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery("from User where username = :name");
        query.setString("name", username);
        return (User) query.uniqueResult();
    }

    @Override
    public boolean userExists(final String username) {
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery("select count(*) from User where username = :name");
        query.setString("name", username);
        Long count = (Long) query.uniqueResult();
        return count.intValue() == 1;
    }

    @Override
    public User addUser(final String username, final String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        Session session = this.getSessionFactory().getCurrentSession();
        session.persist(user);
        List<User> followedBy = user.getFollowedBy();
        if (followedBy == null) {
            followedBy = new ArrayList<User>();
            followedBy.add(user);
            user.setFollows(followedBy);
        } else {
            followedBy.add(user);
        }
        user = (User) session.merge(user);
        return user;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean isFollowedBy(final User user, final User userToFollow) {
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery("select count(*) from User user inner join user.followedBy followed where user.id = :userToFollowId and followed.id = :userId");
        query.setParameter("userToFollowId", userToFollow.getId());
        query.setParameter("userId", user.getId());
        Long count = (Long) query.uniqueResult();
        return count.intValue() == 1;
    }

    @Override
    public void followUser(User user, User userToFollow) {
        Session session = this.getSessionFactory().getCurrentSession();
        user = (User) session.merge(user);
        userToFollow = (User) session.merge(userToFollow);
        List<User> followedBy = userToFollow.getFollowedBy();
        followedBy.add(user);
        session.merge(userToFollow);
    }
}
