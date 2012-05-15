package com.lucho.atmosphere;

import javax.servlet.Filter;

/**
 * 
 * @author Luciano.Leggieri
 *
 */
public final class FilterAndName {
    
    public final Filter f;
    
    public final String name;
    
    public FilterAndName(final Filter filter, final String filterName) {
        f = filter;
        name = filterName;
    }

}
