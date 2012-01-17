package com.lucho.util;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.service.jta.platform.internal.AbstractJtaPlatform;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;

public final class AtomikosJtaPlatform extends AbstractJtaPlatform {

	private TransactionManager utm = new UserTransactionManager();
	
	private UserTransaction userTransaction = new UserTransactionImp();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -74991083213512919L;

	@Override
	protected TransactionManager locateTransactionManager() {
		return utm;
	}

	@Override
	protected UserTransaction locateUserTransaction() {
		return userTransaction;
	}

}
