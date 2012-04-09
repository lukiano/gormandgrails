package com.lucho.controller;

import com.lucho.domain.User;
import com.lucho.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * Controller that provides login related endpoints.
 * @author Luciano.Leggieri
 */
@Controller
public final class LoginController {

    /**
     * User service.
     */
    private final UserRepository userRepository;

    /**
     * I18n messages.
     */
    private final MessageSourceAccessor messages;

    /**
     * Class constructor.
     * @param anUserRepository user repository.
     * @param aMessageSource i18n messages.
     */
    @Inject
    public LoginController(final UserRepository anUserRepository,
                           final MessageSource aMessageSource) {
        this.userRepository = anUserRepository;
        this.messages = new MessageSourceAccessor(aMessageSource);
    }

    /**
     * Creates a new user with the specified user name and password.
     * @param username the name of the new user.
     * @param password the password for the new user.
     * @return a string detailing success or failure.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(final String username, final String password) {
        String returnMessage;
        try {
            User newUser = this.userRepository.addUser(username, password);
            if (newUser == null) {
                returnMessage = messages.getMessage("login.register.failure");
            } else {
                returnMessage = messages.getMessage("login.register.success");
            }
        } catch (TransactionException e) {
            if (e.getMostSpecificCause()
                    instanceof ConstraintViolationException) {
                ConstraintViolationException cve =
                        (ConstraintViolationException) e.getMostSpecificCause();
                ConstraintViolation<?> cv =
                        cve.getConstraintViolations().iterator().next();
                returnMessage = cv.getMessage();
            } else {
                throw e;
            }
        }
        return returnMessage;
    }

    /**
     * Checks if an user with the specified username exists.
     *
     * @param username a string with the user name.
     * @return a string detailing if the user name is free to use.
     */
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseBody
    public String exists(@RequestParam(value = "name") final String username) {
        String message;
        if (this.userRepository.userExists(username)) {
            message = messages.getMessage("login.username.exists");
        } else {
            message = messages.getMessage("login.username.free");
        }
        return message;
    }

}
