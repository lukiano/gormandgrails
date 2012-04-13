package com.lucho.util;

import org.hibernate.search.indexes.impl.DirectoryBasedIndexManager;
import org.hibernate.search.infinispan.CacheManagerServiceProvider;
import org.hibernate.search.infinispan.logging.impl.Log;
import org.hibernate.search.spi.BuildContext;
import org.hibernate.search.store.impl.DirectoryProviderHelper;
import org.hibernate.search.util.configuration.impl.ConfigurationParseHelper;
import org.hibernate.search.util.logging.impl.LoggerFactory;
import org.infinispan.Cache;
import org.infinispan.lucene.InfinispanDirectory;
import org.infinispan.lucene.locking.BaseLockFactory;
import org.infinispan.lucene.locking.TransactionalLockFactory;
import org.infinispan.lucene.readlocks.DistributedSegmentReadLocker;
import org.infinispan.manager.EmbeddedCacheManager;

import java.util.Properties;

/**
 * A DirectoryProvider using Infinispan to store the Index. This depends on the
 * CacheManagerServiceProvider to get a reference to the Infinispan {@link EmbeddedCacheManager}.
 *
 * @author Sanne Grinovero
 */
public class InfinispanDirectoryProvider implements org.hibernate.search.store.DirectoryProvider<InfinispanDirectory> {

    private static final Log log = LoggerFactory.make(Log.class);

    public static final String DEFAULT_LOCKING_CACHENAME = "LuceneIndexesLocking";

    public static final String DEFAULT_INDEXESDATA_CACHENAME = "LuceneIndexesData";

    public static final String DEFAULT_INDEXESMETADATA_CACHENAME = "LuceneIndexesMetadata";

    private BuildContext context;
    private String directoryProviderName;

    private String metadataCacheName;
    private String dataCacheName;
    private String lockingCacheName;
    private int chunkSize;

    private InfinispanDirectory directory;

    private EmbeddedCacheManager cacheManager;

    @Override
    public void initialize(String directoryProviderName, Properties properties, BuildContext context) {
        this.directoryProviderName = directoryProviderName;
        this.context = context;
        metadataCacheName = properties.getProperty( "metadata_cachename", DEFAULT_INDEXESMETADATA_CACHENAME );
        dataCacheName = properties.getProperty( "data_cachename", DEFAULT_INDEXESDATA_CACHENAME );
        lockingCacheName = properties.getProperty( "locking_cachename", DEFAULT_LOCKING_CACHENAME );
        chunkSize = ConfigurationParseHelper.getIntValue(
                properties, "chunk_size", InfinispanDirectory.DEFAULT_BUFFER_SIZE
        );
    }

    @Override
    public void start(DirectoryBasedIndexManager indexManager) {
        log.debug( "Starting InfinispanDirectory" );
        cacheManager = context.requestService( CacheManagerServiceProvider.class );
        cacheManager.startCaches( metadataCacheName, dataCacheName, lockingCacheName );
        Cache metadataCache = cacheManager.getCache( metadataCacheName );
        Cache dataCache = cacheManager.getCache( dataCacheName ); //chunksCache
        Cache lockingCache = cacheManager.getCache( lockingCacheName ); //distLocksCache
        //XXX Here we add the TransactionalLockFactory
        directory = new InfinispanDirectory( metadataCache, dataCache, directoryProviderName,
                //new TransactionalLockFactory(lockingCache,  directoryProviderName),
                new BaseLockFactory(lockingCache, directoryProviderName),
                chunkSize,
                new DistributedSegmentReadLocker(lockingCache, dataCache, metadataCache, directoryProviderName)
                );
        DirectoryProviderHelper.initializeIndexIfNeeded( directory );
        log.debugf( "Initialized Infinispan index: '%s'", directoryProviderName );
    }

    @Override
    public void stop() {
        directory.close();
        context.releaseService( CacheManagerServiceProvider.class );
        log.debug( "Stopped InfinispanDirectory" );
    }

    @Override
    public InfinispanDirectory getDirectory() {
        return directory;
    }

    public EmbeddedCacheManager getCacheManager() {
        return cacheManager;
    }

}
