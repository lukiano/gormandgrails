package com.lucho.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class TwitterMessageListener implements MessageListener {

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(TwitterMessageListener.class);


    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        Integer ownerId;
        try {
            ownerId = mapMessage.getInt("owner");
        } catch (JMSException e) {
            logger.error("While processing message", e);
            return;
        }
        this.getUserService().refreshFollowersFor(ownerId);
    }
}
