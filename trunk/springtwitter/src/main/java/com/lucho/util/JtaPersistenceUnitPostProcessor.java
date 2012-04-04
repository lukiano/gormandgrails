package com.lucho.util;

import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

import javax.persistence.spi.PersistenceUnitTransactionType;

/**
 * @author Luciano Leggieri
 */
public final class JtaPersistenceUnitPostProcessor
        implements PersistenceUnitPostProcessor {

    /**
     * {@inheritDoc}
     */
    public void postProcessPersistenceUnitInfo(
            final MutablePersistenceUnitInfo mutablePersistenceUnitInfo) {
        mutablePersistenceUnitInfo
                .setJtaDataSource(mutablePersistenceUnitInfo
                        .getNonJtaDataSource());
        mutablePersistenceUnitInfo
                .setTransactionType(PersistenceUnitTransactionType.JTA);
    }

}
