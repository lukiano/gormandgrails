package com.lucho.util;

import org.hibernate.search.SearchException;
import org.hibernate.search.backend.LuceneWork;
import org.hibernate.search.backend.OptimizeLuceneWork;
import org.hibernate.search.indexes.serialization.spi.LuceneWorkSerializer;
import org.hibernate.search.indexes.spi.IndexManager;
import org.hibernate.search.util.logging.impl.Log;
import org.hibernate.search.util.logging.impl.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.QueueSender;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luciano
 * Date: 4/16/12
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class JMSBackendQueueTask implements Runnable {

    private static final Log log = LoggerFactory.make();

    public static final String INDEX_NAME_JMS_PROPERTY = "HSearchIndexName";

    private final Collection<LuceneWork> queue;
    private final JMSBackendQueueProcessor processor;
    private final String indexName;
    private final IndexManager indexManager;

    public JMSBackendQueueTask(String indexName, Collection<LuceneWork> queue, IndexManager indexManager,
                               JMSBackendQueueProcessor jmsBackendQueueProcessor) {
        this.indexName = indexName;
        this.queue = queue;
        this.indexManager = indexManager;
        this.processor = jmsBackendQueueProcessor;
    }

    public void run() {
        List<LuceneWork> filteredQueue = new ArrayList<LuceneWork>(queue);
        for (LuceneWork work : queue) {
            if ( work instanceof OptimizeLuceneWork) {
                //we don't want optimization to be propagated
                filteredQueue.remove( work );
            }
        }
        if ( filteredQueue.size() == 0) return;
        LuceneWorkSerializer serializer = indexManager.getSerializer();
        byte[] data = serializer.toSerializedModel( filteredQueue );
        processor.prepareJMSTools();
        Connection cnn = null;
        Session session;
        try {
            cnn = processor.getJMSFactory().createConnection();
            //XXX Since this is called in after completion phase, we cannot use a transaction here, so transacted is false.
            session = cnn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ObjectMessage message = session.createObjectMessage();
            message.setObject( data );
            message.setStringProperty( INDEX_NAME_JMS_PROPERTY, indexName );
            session.createProducer(processor.getJmsQueue()).send(message);

            session.close();
        }
        catch (JMSException e) {
            throw new SearchException( "Unable to send Search work to JMS queue: " + processor.getJmsQueueName(), e );
        }
        finally {
            try {
                if (cnn != null)
                    cnn.close();
            }
            catch ( JMSException e ) {
                log.unableToCloseJmsConnection( processor.getJmsQueueName(), e );
            }
        }
    }
}
