package org.ebsdimage.core.sim;

import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.core.Microscope;
import org.simpleframework.xml.Element;

import crystallography.core.ScatteringFactorsEnum;

/**
 * Metadata for a <code>SimMMap</code>.
 * 
 * @author ppinard
 */
public class SimMetadata extends EbsdMetadata {

    /** Default simulation metadata. */
    public static final SimMetadata DEFAULT = new SimMetadata(new Microscope(),
            ScatteringFactorsEnum.XRAY, 4);

    /** Type of scattering factors. */
    @Element(name = "scatteringFactors")
    public final ScatteringFactorsEnum scatteringFactors;

    /** Maximum index of the planes to compute. */
    @Element(name = "maxIndex")
    public final int maxIndex;



    /**
     * Creates a new <code>SimMetadata</code>.
     * 
     * @param microscope
     *            microscope configuration
     * @param scatteringFactors
     *            type of scattering factors
     * @param maxIndex
     *            maximum index of the planes to compute
     */
    public SimMetadata(
            @Element(name = "microscope") Microscope microscope,
            @Element(name = "scatteringFactors") ScatteringFactorsEnum scatteringFactors,
            @Element(name = "maxIndex") int maxIndex) {
        super(microscope);

        this.scatteringFactors = scatteringFactors;

        if (maxIndex < 1)
            throw new IllegalArgumentException(
                    "The maximum index has to greater or equal to 1.");
        this.maxIndex = maxIndex;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        SimMetadata other = (SimMetadata) obj;
        if (!scatteringFactors.equals(other.scatteringFactors))
            return false;

        if (maxIndex != other.maxIndex)
            return false;

        return true;
    }

}
