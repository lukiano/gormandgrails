package com.lucho.jms;

import com.lucho.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Listens to JMS messages.
 */
public final class TwitterMessageListener implements MessageListener {

    /** Default logger. */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(TwitterMessageListener.class);

    /** user service. */
    private final UserService userService;

    /**
     * Class constructor.
     * @param anUserService user service used for refresh the walls.
     */
    @Autowired
    public TwitterMessageListener(final UserService anUserService) {
        this.userService = anUserService;
    }

    /**
     * Refresh user walls.
     * @param message incoming JMS message.
     */
    @Override
    public void onMessage(final Message message) {
        MapMessage mapMessage = (MapMessage) message;
        try {
            Integer ownerId = mapMessage.getInt("owner");
            this.userService.refreshFollowersFor(ownerId);
        } catch (JMSException e) {
            LOGGER.error("While processing message", e);
        }
    }

}
