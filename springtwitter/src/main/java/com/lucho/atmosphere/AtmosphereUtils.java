package com.lucho.atmosphere;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Meteor;

/**
 * Helper class to handle Atmosphere.
 * @author Luciano.Leggieri
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
        return getMeteor(request).getAtmosphereResource();
    }

    /**
     * Obtains Meteor resources from the request.
     * @param request an HttpServletRequest.
     * @return a {@link Meteor} resource. Never returns null.
     */
    public static Meteor getMeteor(final HttpServletRequest request) {
        return Meteor.build(request);
    }

}
