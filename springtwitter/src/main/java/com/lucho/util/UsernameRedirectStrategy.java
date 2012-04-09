package com.lucho.util;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Luciano.Leggieri
 */
public final class UsernameRedirectStrategy extends DefaultRedirectStrategy {

    /**
     * Key name that holds the last user name.
     */
    private static final String NAME_KEY = "login_name";

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendRedirect(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final String url) throws IOException {
        String username = request.getParameter(
                UsernamePasswordAuthenticationFilter
                        .SPRING_SECURITY_FORM_USERNAME_KEY);
        if (username == null) {
            super.sendRedirect(request, response, url);
        } else {
            String newUrl;
            if (url.indexOf('?') >= 0) {
                newUrl = url + "&" + NAME_KEY + "=" + username;
            } else {
                newUrl = url + "?" + NAME_KEY + "=" + username;
            }
            super.sendRedirect(request, response, newUrl);
        }
    }

}
