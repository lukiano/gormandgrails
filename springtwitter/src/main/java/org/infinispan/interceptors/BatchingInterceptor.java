package org.infinispan.interceptors;

import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.infinispan.batch.BatchContainer;
import org.infinispan.commands.VisitableCommand;
import org.infinispan.context.InvocationContext;
import org.infinispan.context.InvocationContextContainer;
import org.infinispan.factories.annotations.Inject;
import org.infinispan.interceptors.base.CommandInterceptor;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;

/**
 * Interceptor that captures batched calls and attaches contexts.
 *
 * @author Manik Surtani (<a href="mailto:manik@jboss.org">manik@jboss.org</a>)
 * @since 4.0
 */
public class BatchingInterceptor extends CommandInterceptor {
   BatchContainer batchContainer;
   TransactionManager transactionManager;
   InvocationContextContainer icc;

   private static final Log log = LogFactory.getLog(BatchingInterceptor.class);

   @Override
   protected Log getLog() {
      return log;
   }

   @Inject
   private void inject(BatchContainer batchContainer, TransactionManager transactionManager, InvocationContextContainer icc) {
      this.batchContainer = batchContainer;
      this.transactionManager = transactionManager;
      this.icc = icc;
   }

   /**
    * Simply check if there is an ongoing tx. <ul> <li>If there is one, this is a no-op and just passes the call up the
    * chain.</li> <li>If there isn't one and there is a batch in progress, resume the batch's tx, pass up, and finally
    * suspend the batch's tx.</li> <li>If there is no batch in progress, just pass the call up the chain.</li> </ul>
    */
   @Override
   protected Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable {
      Transaction tx;
      if (!ctx.isOriginLocal()) return invokeNextInterceptor(ctx, command);
      // if in a batch, attach tx
      if (transactionManager.getTransaction() == null && (tx = batchContainer.getBatchTransaction()) != null) {
         try {
                //XXX before trying to resume or create a new context, check that the transaction is not committing.
        	if (transactionManager.getStatus() != Status.STATUS_COMMITTED 
        			&& transactionManager.getStatus() != Status.STATUS_COMMITTING &&
        			!ctx.isInTxScope()) {
        		transactionManager.resume(tx);
                //If there's no ongoing tx then BatchingInterceptor creates one and then invokes next interceptor,
                // so that all interceptors in the stack will be executed in a transactional context.
                // This is where a new context (TxInvocationContext) is created, as the existing context is not transactional: NonTxInvocationContext.
                InvocationContext txContext = icc.createInvocationContext(true, -1);
                txContext.setFlags(ctx.getFlags());
                return invokeNextInterceptor(txContext, command);
        	} else {
        		return invokeNextInterceptor(ctx, command);
        	}
         } finally {
            if (transactionManager.getTransaction() != null && batchContainer.isSuspendTxAfterInvocation())
               transactionManager.suspend();
         }
      } else {
         return invokeNextInterceptor(ctx, command);
      }
   }
}
