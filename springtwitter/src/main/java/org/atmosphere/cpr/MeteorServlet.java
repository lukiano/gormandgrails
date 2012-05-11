package org.atmosphere.cpr;

import static org.atmosphere.cpr.ApplicationConfig.FILTER_CLASS;
import static org.atmosphere.cpr.ApplicationConfig.FILTER_NAME;
import static org.atmosphere.cpr.ApplicationConfig.MAPPING;
import static org.atmosphere.cpr.ApplicationConfig.SERVLET_CLASS;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.Meteor;
import org.atmosphere.handler.ReflectorServletProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Servlet to use when Atmosphere {@link Meteor} are used. This Servlet
 * will look for a Servlet init-param named org.atmosphere.servlet or
 * org.atmosphere.filter and will delegate request processing to them. When
 * used, this Servlet will ignore any value defined in META-INF/atmosphere.xml
 * as internally it will create a {@link ReflectorServletProcessor}
 * @author Jean-Francois Arcand
 */
public final class MeteorServlet extends AtmosphereServlet {

    protected static final Logger logger = LoggerFactory
            .getLogger(MeteorServlet.class);

    private final Servlet servlet;

    public MeteorServlet(final Servlet aServlet) {
        super(false, false);
        this.servlet = aServlet;
    }

    @Override
    public void init(final ServletConfig sc) throws ServletException {
        super.init(sc);

        String servletClass = framework().getAtmosphereConfig()
                .getInitParameter(SERVLET_CLASS);
        String mapping = framework().getAtmosphereConfig().getInitParameter(
                MAPPING);
        String filterClass = framework().getAtmosphereConfig()
                .getInitParameter(FILTER_CLASS);
        String filterName = framework().getAtmosphereConfig().getInitParameter(
                FILTER_NAME);

        logger.info("Installed Servlet/Meteor {} mapped to {}", servletClass,
                mapping == null ? "/*" : mapping);

        if (filterClass != null) {
            logger.info("Installed Filter/Meteor {} mapped to /*", filterClass,
                    mapping);
        }

        ReflectorServletProcessor r =
                new ReflectorServletProcessor(this.servlet);
        r.setFilterClassName(filterClass);
        r.setFilterName(filterName);

        if (mapping == null) {
            mapping = "/*";
            BroadcasterFactory.getDefault().remove("/*");
        }
        framework.addAtmosphereHandler(mapping, r).initAtmosphereHandler(sc);
    }

    @Override
    public void destroy() {
        super.destroy();
        Meteor.cache.clear();
    }
}
