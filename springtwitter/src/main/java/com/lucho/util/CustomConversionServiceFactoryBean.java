package com.lucho.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 * Adds {@link DomainClassConverter}
 * to the list of default registered converters.
 *
 * @author Luciano.Leggieri
 */
public class CustomConversionServiceFactoryBean
        extends ConversionServiceFactoryBean
        implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * {@inheritDoc}
     */
    protected GenericConversionService createConversionService() {
        GenericConversionService conversionService
                = new DefaultFormattingConversionService();
        DomainClassConverter domainClassConverter =
                new DomainClassConverter(conversionService);
        domainClassConverter.setApplicationContext(applicationContext);
        conversionService.addConverter(domainClassConverter);
        return conversionService;
    }

    @Override
    public void setApplicationContext(final ApplicationContext ac) {
        this.applicationContext = ac;
    }

}
