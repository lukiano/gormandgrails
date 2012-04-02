package com.lucho.controller;

import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

public class WebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(final ServletContext sc) throws ServletException {

		throw new ServletException("should not be called!");
		/*
		// Create the 'root' Spring application context
		XmlWebApplicationContext root = new XmlWebApplicationContext();

		// Manages the life cycle of the root application context
		sc.addListener(new ContextLoaderListener(root));
		sc.addListener(new HttpSessionEventPublisher());

		// spring security
		DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy("springSecurityFilterChain", root);
		FilterRegistration fr = sc.addFilter("springSecurityFilterChain", delegatingFilterProxy);
		fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/**");

		XmlWebApplicationContext dispatcherServletContext = new XmlWebApplicationContext();
		dispatcherServletContext.setNamespace("tweeter-servlet");
		ServletRegistration.Dynamic dispatcher = sc.addServlet("tweeter", new DispatcherServlet(dispatcherServletContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
		*/
	}
}
