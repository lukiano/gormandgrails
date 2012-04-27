package com.lucho.util;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.utils.Service;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * Delegate so that Spring can shutdown it.
 */
public final class BitronixTransactionManager implements TransactionManager,
        UserTransaction, Service {

    /** delegate */
    private final bitronix.tm.BitronixTransactionManager tm;

    /** Default Class Constructor. */
    public BitronixTransactionManager() {
        tm = TransactionManagerServices.getTransactionManager();
    }

    @Override
    public void begin() throws NotSupportedException, SystemException {
       tm.begin();
    }

    @Override
    public void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException {
        tm.commit();
    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {
        tm.rollback();
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException,
            SystemException {
        tm.setRollbackOnly();
    }

    @Override
    public int getStatus() throws SystemException {
        return tm.getStatus();
    }

    @Override
    public Transaction getTransaction() throws SystemException {
        return tm.getTransaction();
    }

    @Override
    public void setTransactionTimeout(int seconds) throws SystemException {
        tm.setTransactionTimeout(seconds);
    }

    @Override
    public Transaction suspend() throws SystemException {
        return tm.suspend();
    }

    @Override
    public void resume(Transaction tobj) throws InvalidTransactionException,
            IllegalStateException, SystemException {
       tm.resume(tobj);
    }

    public void shutdown() {
        tm.shutdown();
    }
}
