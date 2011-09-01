package com.lucho.service;

import com.lucho.dao.UserDao;
import com.lucho.domain.User;
import org.infinispan.util.concurrent.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/29/11
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class TwitterMessageListener implements MessageListener {

    public ConcurrentHashSet<Integer> getUsersToBeRefreshed() {
        return usersToBeRefreshed;
    }

    public void setUsersToBeRefreshed(ConcurrentHashSet<Integer> usersToBeRefreshed) {
        this.usersToBeRefreshed = usersToBeRefreshed;
    }

    private ConcurrentHashSet<Integer> usersToBeRefreshed = new ConcurrentHashSet<Integer>();

    public UserDao getUserDao() {
        return userDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private UserDao userDao;

    private static Logger logger = LoggerFactory.getLogger(TwitterMessageListener.class);


    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        Integer ownerId = null;
        try {
            ownerId = mapMessage.getInt("owner");
        } catch (JMSException e) {
            logger.error("While processing message", e);
            return;
        }
        User user = this.getUserDao().getUser(ownerId);
        List<User> followedByList = user.getFollowedBy();
        for (User follower : followedByList) {
               usersToBeRefreshed.add(follower.getId());
        }
    }
}
