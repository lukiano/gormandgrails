package com.lucho.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 * Adds {@link DomainClassConverter} to the list of default registered
 * converters.
 * @author Luciano.Leggieri
 */
public final class CustomConversionServiceFactoryBean extends
        ConversionServiceFactoryBean implements ApplicationContextAware {

    /**
     * Spring appcontext is needed by the converter.
     */
    private ApplicationContext applicationContext;

    /**
     * {@inheritDoc}
     */
    protected GenericConversionService createConversionService() {
        DefaultFormattingConversionService conversionService =
                new DefaultFormattingConversionService();
        DomainClassConverter<DefaultFormattingConversionService>
            domainClassConverter =
            new DomainClassConverter<DefaultFormattingConversionService>(
                    conversionService);
        domainClassConverter.setApplicationContext(applicationContext);
        conversionService.addConverter(domainClassConverter);
        return conversionService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(final ApplicationContext ac) {
        this.applicationContext = ac;
    }

}
