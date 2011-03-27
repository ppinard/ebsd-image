package org.ebsdimage.core.run;

import net.jcip.annotations.Immutable;

import org.simpleframework.xml.Root;

/**
 * A run operation.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
@Root
public interface Operation {

    /**
     * Returns the name of this operation.
     * 
     * @return name
     */
    public String getName();

}
