package com.lucho.util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.search.backend.impl.jms.AbstractJMSHibernateSearchController;

/**
 * This class will be used in a cluster environment where Hibernate Search is
 * set to communicate by JMS. This class goes in the Master node.
 *
 * @author Luciano.Leggieri
 *
 */
public final class JMSHibernateSearchController extends
        AbstractJMSHibernateSearchController {

    /**
     * Hibernate backed Entity Manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected Session getSession() {
        return (Session) entityManager.getDelegate();
    }

    @Override
    protected void cleanSessionIfNeeded(final Session session) {
    }

}
