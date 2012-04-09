package com.lucho.util;

import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 * Adds {@link DomainClassConverter}
 * to the list of default registered converters.
 */
public final class CustomConversionServiceFactoryBean
        extends ConversionServiceFactoryBean {

    /**
     * {@inheritDoc}
     */
    protected GenericConversionService createConversionService() {
        super.createConversionService();
        GenericConversionService conversionService
                = new DefaultFormattingConversionService();
        conversionService.addConverter(
                new DomainClassConverter(conversionService));
        return conversionService;
    }

}
