package com.lucho.util;

import javax.naming.InitialContext;
import javax.transaction.TransactionManager;

import org.infinispan.transaction.lookup.TransactionManagerLookup;

/**
 * Lookup that obtains the managers from JNDI.
 * @author Luciano.Leggieri
 */
public final class JndiTransactionManagerLookup implements
        TransactionManagerLookup {

    /**
     * Transaction Manager JNDI name.
     */
    public static final String TM_NAME = "env/TransactionManager";

    @Override
    public TransactionManager getTransactionManager() throws Exception {
        InitialContext initialContext = new InitialContext();
        return (TransactionManager) initialContext.lookup(TM_NAME);
    }

}
