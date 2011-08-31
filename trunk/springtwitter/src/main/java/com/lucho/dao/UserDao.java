package com.lucho.dao;

import com.lucho.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.util.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/30/11
 * Time: 9:48 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UserDao {

    private SessionFactory sessionFactory;

    public User getUser(final Integer id) {
        Session session = this.getSessionFactory().getCurrentSession();
        return (User) session.get(User.class, id);
    }

    @SuppressWarnings("unchecked")
    public User getUser(final String username) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("username", username));
        Session session = this.getSessionFactory().getCurrentSession();
        List<User> result = criteria.getExecutableCriteria(session).list();
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    public User addUser(final String username, final String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        Session session = this.getSessionFactory().getCurrentSession();
        session.persist(user);
        return user;
    }

    @Autowired
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
