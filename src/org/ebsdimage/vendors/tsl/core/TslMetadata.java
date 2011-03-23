package org.ebsdimage.vendors.tsl.core;

import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.core.Microscope;
import org.simpleframework.xml.Element;

/**
 * Metadata held in a <code>TslMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class TslMetadata extends EbsdMetadata {

    /**
     * Creates a <code>HklMetadata</code> with the required parameters.
     * 
     * @param microscope
     *            microscope configuration
     */
    public TslMetadata(@Element(name = "microscope") Microscope microscope) {
        super(microscope);
    }

}
