package com.lucho.atmosphere;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.FrameworkConfig;

/**
 * Helper class to handle Atmosphere.
 * @author Luciano.Leggieri
 *
 */
public final class AtmosphereUtils {

    /**
     * Private Constructor to avoid instantiation.
     */
    private AtmosphereUtils() {
    }

    /**
     * Obtains Atmosphere resources from the request.
     * @param request an HttpServletRequest.
     * @return an Atmosphere resource. Should never return null.
     */
    public static AtmosphereResource getAtmosphereResource(
            final HttpServletRequest request) {
        AtmosphereResource resource = (AtmosphereResource) request
                .getAttribute(FrameworkConfig.ATMOSPHERE_RESOURCE);
        return resource;
    }

}
