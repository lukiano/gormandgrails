package com.lucho.controller;

import com.lucho.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by IntelliJ IDEA.
 * User: lucianol
 * Date: 8/31/11
 * Time: 11:31 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class LoginController {

    @Autowired
    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private UserService userService;

    @RequestMapping(value = "/pepe", method = RequestMethod.GET)
    public String pepe() {
         return "pepe2";
    }

    @RequestMapping(value = "/pepe2", method = RequestMethod.GET)
    public @ResponseBody String pepe2() {
         return "pepe3";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(String username, String password) {

    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public void signup(String username, String password) {

    }

}
