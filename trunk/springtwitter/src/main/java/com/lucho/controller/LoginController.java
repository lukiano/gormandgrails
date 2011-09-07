package com.lucho.controller;

import com.lucho.domain.User;
import com.lucho.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Controller
final class LoginController {

    private UserService userService;

    @Autowired
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(final String username, final String password) {
        try {
            User newUser = this.userService.addUser(username, password);
            if (newUser == null) {
                return "User already exists";
            } else {
                return "User added successfully";
            }
        } catch (TransactionException e) {
            if (e.getMostSpecificCause() instanceof ConstraintViolationException) {
                ConstraintViolationException cve = (ConstraintViolationException) e.getMostSpecificCause();
                ConstraintViolation<?> cv = cve.getConstraintViolations().iterator().next();
                return cv.getMessage();
            } else {
                throw e;
            }
        }
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseBody
    public Boolean exists(@RequestParam(value = "name") final String username) {
        return this.userService.userExists(username);
    }

}
