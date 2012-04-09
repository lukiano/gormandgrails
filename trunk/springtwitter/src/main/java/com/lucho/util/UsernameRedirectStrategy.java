package com.lucho.util;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: luciano
 * Date: 4/5/12
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsernameRedirectStrategy extends DefaultRedirectStrategy {

    @Override
    public void sendRedirect(final HttpServletRequest request,
                             final HttpServletResponse response, final String url) throws IOException {
        String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
        if (username == null) {
            super.sendRedirect(request, response, url);
        } else {
            String newUrl;
            if (url.indexOf('?') >= 0) {
                newUrl = url + "&login_name=" + username;
            } else {
                newUrl = url + "?login_name=" + username;
            }
            super.sendRedirect(request, response, newUrl);
        }
    }

}
