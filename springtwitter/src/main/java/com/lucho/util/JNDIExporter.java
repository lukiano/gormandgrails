package com.lucho.util;

import org.springframework.beans.factory.InitializingBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

/**
 * Exports a Spring bean to JNDI.
 * @author Luciano.Leggieri
 */
public final class JNDIExporter implements InitializingBean {

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
     *
     * @param binding  Object to export to JNDI.
     * @param bindName JNDI name.
     */
    public JNDIExporter(final Object binding, final String bindName) {
        this.name = bindName;
        this.objectToBind = binding;
    }

    /**
     * Export object to JNDI.
     *
     * @throws Exception if an exception occurs while binding.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Context initialContext = new InitialContext();
        Context compContext;
        try {
            compContext = (Context) initialContext.lookup("comp");
        } catch (NameNotFoundException e) {
            compContext = initialContext.createSubcontext("comp");
        }
        compContext.bind(this.name, this.objectToBind);
    }

}
