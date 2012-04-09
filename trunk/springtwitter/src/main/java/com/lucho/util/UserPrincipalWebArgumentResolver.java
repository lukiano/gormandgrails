package com.lucho.util;

import com.lucho.controller.Principal;
import com.lucho.domain.User;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Gets the current User domain object from the {@link Principal}.
 * @author Luciano.Leggieri
 */
public final class UserPrincipalWebArgumentResolver
        implements WebArgumentResolver {

    /**
     * {@inheritDoc}
     */
    public Object resolveArgument(final MethodParameter methodParameter,
                                  final NativeWebRequest webRequest) {
        Object resolved = UNRESOLVED;
        if (methodParameter.getParameterType().equals(User.class)
                && methodParameter.hasParameterAnnotation(Principal.class)) {
            HttpServletRequest request =
                    webRequest.getNativeRequest(HttpServletRequest.class);
            Authentication authentication =
                    (Authentication) request.getUserPrincipal();
            if (authentication != null) {
                resolved = authentication.getPrincipal();
            }
        }
        return resolved;
    }
}
