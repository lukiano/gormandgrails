package com.lucho.util;

import java.util.Locale;

/**
 * Class that returns the ability to cache by locale.
 *
 * @author Luciano.Leggieri
 */
public final class TilesViewResolver extends
        org.springframework.web.servlet.view.tiles2.TilesViewResolver {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<TilesView> requiredViewClass() {
        super.requiredViewClass();
        return TilesView.class;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getCacheKey(final String viewName, final Locale locale) {
        super.getCacheKey(viewName, locale);
        return viewName + "_" + locale;
    }

}
