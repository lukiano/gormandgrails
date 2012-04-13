package bitronix.tm.resource.infinispan;

import bitronix.tm.resource.common.AbstractXAResourceHolder;
import bitronix.tm.resource.common.ResourceBean;
import bitronix.tm.resource.common.XAResourceHolder;

import javax.transaction.xa.XAResource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class InfinispanXAResourceHolder extends AbstractXAResourceHolder {

    private final XAResource resource;
    private final ResourceBean bean;

    /**
     * Create a new InfinispanXAResourceHolder for a particular XAResource
     *
     * @param resource the required XAResource
     * @param bean     the required ResourceBean
     */
    public InfinispanXAResourceHolder(final XAResource resource,
                                      final ResourceBean bean) {
        this.resource = resource;
        this.bean = bean;
    }

    /**
     * {@inheritDoc}
     */
    public XAResource getXAResource() {
        return resource;
    }

    /**
     * {@inheritDoc}
     */
    public ResourceBean getResourceBean() {
        return bean;
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws Exception {
        throw new UnsupportedOperationException("InfinispanXAResourceHolder cannot be used with an XAPool");
    }

    /**
     * {@inheritDoc}
     */
    public Object getConnectionHandle() throws Exception {
        throw new UnsupportedOperationException("InfinispanXAResourceHolder cannot be used with an XAPool");
    }

    /**
     * {@inheritDoc}
     */
    public Date getLastReleaseDate() {
        throw new UnsupportedOperationException("InfinispanXAResourceHolder cannot be used with an XAPool");
    }

    /**
     * {@inheritDoc}
     */
    public List<XAResourceHolder> getXAResourceHolders() {
        List<XAResourceHolder> xaResourceHolders =
                new ArrayList<XAResourceHolder>(1);
        xaResourceHolders.add(this);
        return xaResourceHolders;
    }

}
