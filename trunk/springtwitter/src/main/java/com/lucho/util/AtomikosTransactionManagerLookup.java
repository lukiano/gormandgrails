package com.lucho.util;

import com.atomikos.icatch.jta.J2eeTransactionManager;
import org.infinispan.transaction.lookup.TransactionManagerLookup;

import javax.transaction.TransactionManager;

/**
 * @author Luciano.Leggieri
 */
public class AtomikosTransactionManagerLookup implements TransactionManagerLookup {

    private TransactionManager tm = new J2eeTransactionManager();

    @Override
    public TransactionManager getTransactionManager() throws Exception {
        return tm;
    }
}
