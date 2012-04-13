package bitronix.tm.resource.infinispan;

import bitronix.tm.recovery.RecoveryException;
import bitronix.tm.resource.ResourceRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Luciano.Leggieri
 */
public final class BitronixInfinispanLoader {

    /**
     * Logger.
     */
    private static final Logger LOG
            = LoggerFactory.getLogger(InfinispanXAResourceProducer.class);

    /**
     * Producer's unique name inside Bitronix's Set.
     */
    private static final String PRODUCER_NAME = "infinispan";

    /**
     * Default constructor.
     *
     * @throws RecoveryException when an error happens during recovery.
     */
    public BitronixInfinispanLoader() throws RecoveryException {
        InfinispanXAResourceProducer irp =
                new InfinispanXAResourceProducer();
        irp.setUniqueName(PRODUCER_NAME);
        ResourceRegistrar.register(irp);
        LOG.info("InfinispanXAResourceProducer registered successfully.");
    }

}
