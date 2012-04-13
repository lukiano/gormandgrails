package bitronix.tm.resource.infinispan;

import bitronix.tm.TransactionManagerServices;
import org.infinispan.transaction.lookup.TransactionManagerLookup;

import javax.transaction.TransactionManager;

public class BitronixTransactionManagerLookup implements TransactionManagerLookup {
    @Override
    public TransactionManager getTransactionManager() throws Exception {
        return TransactionManagerServices.getTransactionManager();
    }
}
