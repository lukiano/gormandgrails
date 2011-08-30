package com.lucho.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by IntelliJ IDEA.
 * User: luciano
 * Date: 28/08/11
 * Time: 23:24
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class Prueba {

    @RequestMapping("/helloworld")
    public @ResponseBody String hello() {
        return "Hello world";
    }

    @RequestMapping("/")
    public @ResponseBody String root() {
        return "Hello root!";
    }

}
