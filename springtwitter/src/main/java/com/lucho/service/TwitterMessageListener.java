package com.lucho.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public final class TwitterMessageListener implements MessageListener {

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterMessageListener.class);


    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        Integer ownerId;
        try {
            ownerId = mapMessage.getInt("owner");
        } catch (JMSException e) {
            LOGGER.error("While processing message", e);
            return;
        }
        this.userService.refreshFollowersFor(ownerId);
    }
}
