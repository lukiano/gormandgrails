package com.lucho.util;

import com.lucho.domain.Tweet;
import org.hibernate.search.analyzer.Discriminator;

/**
 * Class used to get the right Lucene Analyzer for the tweet language.
 */
public final class LanguageDiscriminator implements Discriminator {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAnalyzerDefinitionName(
            final Object value, final Object entity, final String field) {
        String returnValue = null;
        if (entity instanceof Tweet) {
            returnValue = (String) value;
        }
        return returnValue;
    }
}

