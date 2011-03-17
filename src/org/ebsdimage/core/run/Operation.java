package org.ebsdimage.core.run;

import org.simpleframework.xml.Root;

/**
 * A run operation.
 * 
 * @author ppinard
 */
@Root
public interface Operation {

    /**
     * Returns the name of this operation.
     * 
     * @return name
     */
    public String getName();

}
