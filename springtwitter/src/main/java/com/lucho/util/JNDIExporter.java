package com.lucho.util;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

/**
 * Exports a Spring bean to JNDI.
 * @author Luciano.Leggieri
 */
public final class JNDIExporter {

    /**
     * @param shouldBindLater true if the binding happens during
     *            {@link PostConstruct}
     */
    private final boolean bindLater;

    /**
     * JNDI name.
     */
    private final String name;

    /**
     * Object to bind.
     */
    private final Object objectToBind;

    /**
     * Class constructor.
     * @param binding Object to export to JNDI.
     * @param bindName JNDI name.
     */
    public JNDIExporter(final Object binding, final String bindName) {
        this(binding, bindName, false);
    }

    /**
     * Class constructor.
     * @param binding Object to export to JNDI.
     * @param bindName JNDI name.
     * @param shouldBindLater true if the binding happens during
     *            {@link PostConstruct}
     */
    public JNDIExporter(final Object binding, final String bindName,
            final boolean shouldBindLater) {
        this.name = bindName;
        this.objectToBind = binding;
        this.bindLater = shouldBindLater;
        if (!this.bindLater) {
            try {
                this.doBinding();
            } catch (NamingException e) {
                throw new IllegalArgumentException(bindName, e);
            }
        }
    }

    /**
     * Export object to JNDI.
     * @throws NamingException if an exception occurs while binding.
     */
    @PostConstruct
    public void afterPropertiesSet() throws NamingException {
        if (this.bindLater) {
            this.doBinding();
        }
    }

    /**
     * Export object to JNDI.
     * @throws NamingException if an exception occurs while binding.
     */
    private void doBinding() throws NamingException {
        Context initialContext = new InitialContext();
        Context compContext;
        try {
            compContext = (Context) initialContext.lookup("env");
        } catch (NameNotFoundException e) {
            compContext = initialContext.createSubcontext("env");
        }
        compContext.bind(this.name, this.objectToBind);
    }

}
