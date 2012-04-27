package com.lucho.util;

import com.atomikos.icatch.jta.J2eeTransactionManager;
import com.atomikos.icatch.jta.J2eeUserTransaction;
import org.hibernate.service.jta.platform.internal.AbstractJtaPlatform;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * {@link org.hibernate.service.jta.platform.spi.JtaPlatform} for
 * Atomikos TM.
 *
 * @author Luciano.Leggieri
 */
public final class AtomikosJtaPlatform extends AbstractJtaPlatform {

    /**
     * Atomikos TransactionManager.
     */
    private TransactionManager tm;

    /**
     * Atomikos UserTransaction.
     */
    private UserTransaction ut;

    /**
     * {@inheritDoc}
     */
    @Override
    protected TransactionManager locateTransactionManager() {
        if (tm == null) {
            tm = new J2eeTransactionManager();
        }
        return tm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UserTransaction locateUserTransaction() {
        if (ut == null) {
            ut = new J2eeUserTransaction();
        }
        return ut;
    }
}
