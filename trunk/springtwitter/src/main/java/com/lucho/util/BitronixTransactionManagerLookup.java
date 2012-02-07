package com.lucho.util;

import javax.transaction.TransactionManager;

import org.infinispan.transaction.lookup.TransactionManagerLookup;

import bitronix.tm.TransactionManagerServices;

public final class BitronixTransactionManagerLookup implements TransactionManagerLookup {
	
	@Override
	public TransactionManager getTransactionManager() throws Exception {
		return TransactionManagerServices.getTransactionManager();
	}

}
