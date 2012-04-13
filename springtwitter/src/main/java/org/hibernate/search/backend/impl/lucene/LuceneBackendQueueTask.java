package org.hibernate.search.backend.impl.lucene;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

import org.apache.lucene.index.IndexWriter;
import org.hibernate.search.backend.IndexingMonitor;
import org.hibernate.search.backend.LuceneWork;
import org.hibernate.search.util.logging.impl.LoggerFactory;
import org.hibernate.search.util.logging.impl.Log;
import org.hibernate.search.exception.impl.ErrorContextBuilder;

/**
 * Apply the operations to Lucene directories.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 * @author John Griffin
 * @author Sanne Grinovero
 */
final class LuceneBackendQueueTask implements Runnable {

    private static final Log log = LoggerFactory.make();

    private final Lock modificationLock;
    private final LuceneBackendResources resources;
    private final List<LuceneWork> queue;
    private final IndexingMonitor monitor;

    LuceneBackendQueueTask(List<LuceneWork> queue, LuceneBackendResources resources, IndexingMonitor monitor) {
        this.queue = queue;
        this.resources = resources;
        this.monitor = monitor;
        this.modificationLock = resources.getParallelModificationLock();
    }

    public void run() {
        modificationLock.lock();
        try {
            applyUpdates();
        } catch ( InterruptedException e ) {
            log.interruptedWhileWaitingForIndexActivity( e );
            Thread.currentThread().interrupt();
            handleException( e );
        } catch ( Exception e ) {
            log.backendError( e );
            handleException( e );
        }
        finally {
            modificationLock.unlock();
        }
    }

    private void handleException(Exception e) {
        ErrorContextBuilder builder = new ErrorContextBuilder();
        builder.allWorkToBeDone( queue );
        builder.errorThatOccurred( e );
        resources.getErrorHandler().handle( builder.createErrorContext() );
    }

    /**
     * Applies all modifications to the index in parallel using the workers executor
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void applyUpdates() throws InterruptedException, ExecutionException {
        AbstractWorkspaceImpl workspace = resources.getWorkspace();

        ErrorContextBuilder errorContextBuilder = new ErrorContextBuilder();
        errorContextBuilder.allWorkToBeDone( queue );

        IndexWriter indexWriter = workspace.getIndexWriter( errorContextBuilder );
        if ( indexWriter == null ) {
            log.cannotOpenIndexWriterCausePreviousError();
            return;
        }
        LinkedList<LuceneWork> failedUpdates = null;
        try {
            for (LuceneWork unit : queue) {
                SingleTaskRunnable task = new SingleTaskRunnable( unit, resources, indexWriter, monitor );
                try {
                    task.run();
                } catch (Exception e) {
                    if ( failedUpdates == null ) {
                        failedUpdates = new LinkedList<LuceneWork>();
                    }
                    failedUpdates.add(unit);
                    errorContextBuilder.errorThatOccurred( e.getCause() );
                }
            }
            if ( failedUpdates != null ) {
                errorContextBuilder.addAllWorkThatFailed( failedUpdates );
                resources.getErrorHandler().handle( errorContextBuilder.createErrorContext() );
            }
            else {
                workspace.optimizerPhase();
            }
        }
        finally {
            workspace.afterTransactionApplied( failedUpdates != null, false );
        }
    }

}
