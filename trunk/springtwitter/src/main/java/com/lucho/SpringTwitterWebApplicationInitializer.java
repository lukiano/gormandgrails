package com.lucho;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.atmosphere.cpr.MeteorServlet;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextCleanupListener;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import static java.lang.Boolean.TRUE;

/**
 * @author Luciano.Leggieri
 */
public final class SpringTwitterWebApplicationInitializer implements
        WebApplicationInitializer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStartup(final ServletContext servletContext)
            throws ServletException {
        ContextLoader contextLoader = new ContextLoader();
        ConfigurableWebApplicationContext rootWac =
                (ConfigurableWebApplicationContext) contextLoader
                .initWebApplicationContext(servletContext);

        ConfigurableWebApplicationContext repositoryWac = buildContext(
                servletContext, rootWac, "/WEB-INF/jpa-config.xml", true);
        ConfigurableWebApplicationContext securityWac = buildContext(
                servletContext, repositoryWac, "/WEB-INF/security-config.xml",
                true);
        ConfigurableWebApplicationContext integrationWac = buildContext(
                servletContext, securityWac, "/WEB-INF/integration-config.xml",
                true);
        ConfigurableWebApplicationContext servletWac = buildContext(
                servletContext, integrationWac, "/WEB-INF/tweeter-servlet.xml",
                false);

        servletContext.addListener(HttpSessionEventPublisher.class);
        servletContext.addListener(ContextCleanupListener.class);

        if (rootWac.getEnvironment().acceptsProfiles("websockets")) {
            Filter securityFilter = new DelegatingFilterProxy(
                    "springSecurityFilterChain", securityWac);
            Servlet dispatcherServlet = new DispatcherServlet(servletWac);
            MeteorServlet meteorServlet = new MeteorServlet(dispatcherServlet);
            meteorServlet
                    .addFilter(securityFilter, "springSecurityFilterChain");
            ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                    "tweeter", meteorServlet);
            dispatcher.setInitParameter("org.atmosphere.cpr.broadcasterClass",
                    "org.atmosphere.cpr.DefaultBroadcaster");
            dispatcher.setInitParameter(
                    "org.atmosphere.cpr.CometSupport.maxInactiveActivity",
                    "30");
            dispatcher.setInitParameter("org.atmosphere.useStream",
                    TRUE.toString());
            dispatcher.setInitParameter("org.atmosphere.useWebSocket",
                    TRUE.toString());
            dispatcher.setInitParameter("org.atmosphere.useNative",
                    TRUE.toString());
            dispatcher.setInitParameter(
                    "org.atmosphere.cpr.broadcaster.shareableThreadPool",
                    TRUE.toString());
            dispatcher.setInitParameter(
                    "org.atmosphere.cpr.broadcasterLifeCyclePolicy", "IDLE");
            dispatcher.setInitParameter(
                    "org.atmosphere.cpr.AtmosphereInterceptor",
                    "org.atmosphere.interceptor.JSONPAtmosphereInterceptor,"
                    + "org.atmosphere.interceptor.SSEAtmosphereInterceptor");

            dispatcher.setLoadOnStartup(1);
            dispatcher.addMapping("/");
        } else {
            FilterRegistration fr = servletContext.addFilter(
                    "springSecurityFilterChain", new DelegatingFilterProxy(
                            "springSecurityFilterChain", securityWac));
            fr.addMappingForUrlPatterns(
                    EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD),
                    false, "/*");

            ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                    "tweeter", new DispatcherServlet(servletWac));
            dispatcher.setLoadOnStartup(1);
            dispatcher.addMapping("/");
        }

    }

    /**
     * Builds a web application context.
     * @param servletContext servlet context.
     * @param parent parent web application context.
     * @param configLocation location of the config xml.
     * @param refresh true if the context should be activated.
     * @return a Spring's web application context with its beans loaded from the
     *         location file.
     */
    private ConfigurableWebApplicationContext buildContext(
            final ServletContext servletContext,
            final ConfigurableWebApplicationContext parent,
            final String configLocation, final boolean refresh) {
        XmlWebApplicationContext wac = new XmlWebApplicationContext() {

            @Override
            protected String[] getDefaultConfigLocations() {
                return new String[] {configLocation};
            }

            @Override
            protected void onClose() {
                super.onClose();
                parent.close();
            }
        };
        wac.setParent(parent);
        wac.setServletContext(servletContext);
        if (refresh) {
            wac.refresh();
        }
        wac.registerShutdownHook();
        return wac;
    }

}
