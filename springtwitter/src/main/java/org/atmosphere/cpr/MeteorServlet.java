package org.atmosphere.cpr;

import static org.atmosphere.cpr.ApplicationConfig.MAPPING;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.atmosphere.handler.ReflectorServletProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lucho.atmosphere.FilterAndName;

/**
 * Simple Servlet to use when Atmosphere {@link Meteor} are used. This Servlet
 * will look for a Servlet init-param named org.atmosphere.servlet or
 * org.atmosphere.filter and will delegate request processing to them. When
 * used, this Servlet will ignore any value defined in META-INF/atmosphere.xml
 * as internally it will create a {@link ReflectorServletProcessor}
 * @author Jean-Francois Arcand
 */
public final class MeteorServlet extends AtmosphereServlet {


    /**
     * Default Serial Version UID.
     */
    private static final long serialVersionUID = 3294253087931323969L;

    /**
     * Logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(MeteorServlet.class);

    /**
     * Servlet to add Meteor.
     */
    private final Servlet servlet;
    
    private final List<FilterAndName> filters = new ArrayList<FilterAndName>();
    
    /**
     * Class constructor.
     * @param aServlet servlet to add meteor.
     */
    public MeteorServlet(final Servlet aServlet) {
        super(false, false);
        this.servlet = aServlet;
    }

    public void addFilter(final Filter f, final String filterName) {
        this.filters.add(new FilterAndName(f, filterName));
    }
    

    @Override
    public void init(final ServletConfig sc) throws ServletException {
        super.init(sc);

        String servletClass = servlet.getClass().getName();
        String mapping = framework().getAtmosphereConfig().getInitParameter(
                MAPPING);
//        String filterClass = framework().getAtmosphereConfig()
//                .getInitParameter(FILTER_CLASS);
//        String filterName = framework().getAtmosphereConfig().getInitParameter(
//                FILTER_NAME);
        if (mapping == null) {
            mapping = "/[a-zA-Z0-9-&_.=;\\?]+";
            BroadcasterFactory.getDefault().remove("/*");
        }
        LOGGER.info("Installed Servlet/Meteor {} mapped to {}", servletClass,
                mapping);

//        if (filterClass != null) {
//            LOGGER.info("Installed Filter/Meteor {} mapped to /*", filterClass,
//                    mapping);
//        }

        ReflectorServletProcessor r =
                new ReflectorServletProcessor(this.servlet);
        r.setServletClassName(servletClass);
//        r.setFilterClassName(filterClass);
//        r.setFilterName(filterName);
        for (FilterAndName filter : filters) {
            r.addFilter(filter);
        }

        framework.addAtmosphereHandler(mapping, r).initAtmosphereHandler(sc);
    }

    @Override
    public void destroy() {
        super.destroy();
        Meteor.cache.clear();
    }
}
