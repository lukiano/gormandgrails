package com.lucho.domain;

import javax.annotation.Nonnull;

/**
 * Domain object that implement this class have an integer that identifies them.
 */
public interface Identifiable {

    /**
     * @return the object's identity
     */
    @Nonnull
    Integer getId();

}
