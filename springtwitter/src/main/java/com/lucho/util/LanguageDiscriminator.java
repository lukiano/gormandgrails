package com.lucho.util;

import com.lucho.domain.Tweet;
import org.hibernate.search.analyzer.Discriminator;

public class LanguageDiscriminator implements Discriminator {

	@Override
	public String getAnalyzerDefinitionName(Object value, Object entity, String field) {
		if ( value == null || !( entity instanceof Tweet) ) {
			return null;
		}
		return (String) value;
	}
}

