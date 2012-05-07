package com.lucho.util;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.service.jta.platform.internal.AbstractJtaPlatform;

/**
 * JTA Platform that obtains the managers from JNDI.
 * @author Luciano.Leggieri
 *
 */
public final class JndiJtaPlatform extends AbstractJtaPlatform {

    /**
     * Version Id.
     */
    private static final long serialVersionUID = 4427679857884952152L;

    /**
     * Transaction Manager JNDI name.
     */
    public static final String TM_NAME = "env/TransactionManager";

    /**
     * User transaction JNDI name.
     */
    public static final String UT_NAME = "env/UserTransaction";

    @Override
    protected TransactionManager locateTransactionManager() {
        return (TransactionManager) jndiService().locate(TM_NAME);
    }

    @Override
    protected UserTransaction locateUserTransaction() {
        return (UserTransaction) jndiService().locate(UT_NAME);
    }
}
