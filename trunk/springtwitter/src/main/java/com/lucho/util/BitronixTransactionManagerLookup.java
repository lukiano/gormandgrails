package com.lucho.util;

import bitronix.tm.TransactionManagerServices;
import org.infinispan.transaction.lookup.TransactionManagerLookup;

import javax.transaction.TransactionManager;

/**
 * Return the Bitronix Transaction Manager.
 *
 * @see TransactionManagerServices
 */
public final class BitronixTransactionManagerLookup
        implements TransactionManagerLookup {

    /**
     * {@inheritDoc}
     */
    @Override
    public TransactionManager getTransactionManager() throws Exception {
        return TransactionManagerServices.getTransactionManager();
    }

}
