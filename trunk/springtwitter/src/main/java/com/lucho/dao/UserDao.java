package com.lucho.dao;

import com.lucho.domain.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.util.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
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
public class UserDao extends HibernateDaoSupport {

    public User getUser(final Integer id) {
        return this.getHibernateTemplate().get(User.class, id);
    }

    public User getUser(final String nickname) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("nickname", nickname));
        List<User> result = this.getHibernateTemplate().findByCriteria(criteria);
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    public User addUser(final String nickname, final String password) {
        User user = new User();
        user.setNickname(nickname);
        user.setPassword(password);
        this.getHibernateTemplate().persist(user);
        return user;
    }

}
