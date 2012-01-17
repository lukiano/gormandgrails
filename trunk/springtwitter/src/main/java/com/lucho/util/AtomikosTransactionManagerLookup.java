package com.lucho.util;

import javax.transaction.TransactionManager;

import org.infinispan.transaction.lookup.TransactionManagerLookup;

import com.atomikos.icatch.jta.UserTransactionManager;

public final class AtomikosTransactionManagerLookup implements TransactionManagerLookup {
	
	private TransactionManager utm = new UserTransactionManager();

	@Override
	public TransactionManager getTransactionManager() throws Exception {
		return utm;
	}

}
