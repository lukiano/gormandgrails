package com.lucho.atmosphere;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author Luciano.Leggieri
 */
public final class AtmosphereResourceArgumentResolver implements
        WebArgumentResolver {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object resolveArgument(final MethodParameter methodParameter,
            final NativeWebRequest webRequest) throws Exception {

        if (AtmosphereResource.class.isAssignableFrom(methodParameter
                .getParameterType())) {
            return AtmosphereUtils.getAtmosphereResource(webRequest
                    .getNativeRequest(HttpServletRequest.class));
        } else {
            return WebArgumentResolver.UNRESOLVED;
        }

    }

}
