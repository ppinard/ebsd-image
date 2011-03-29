/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ebsdimage.core.sim;

import org.ebsdimage.core.AcquisitionConfig;
import org.ebsdimage.core.EbsdMetadata;
import org.simpleframework.xml.Element;

import crystallography.core.ScatteringFactorsEnum;

/**
 * Metadata for a <code>SimMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class SimMetadata extends EbsdMetadata {

    /** Default simulation metadata. */
    public static final SimMetadata DEFAULT = new SimMetadata(
            AcquisitionConfig.DEFAULT, ScatteringFactorsEnum.XRAY, 4);

    /** Type of scattering factors. */
    @Element(name = "scatteringFactors")
    private ScatteringFactorsEnum scatteringFactors;

    /** Maximum index of the planes to compute. */
    @Element(name = "maxIndex")
    private int maxIndex;



    /**
     * Creates a new <code>SimMetadata</code>.
     * 
     * @param acqConfig
     *            acquisition configuration
     * @param scatteringFactors
     *            type of scattering factors
     * @param maxIndex
     *            maximum index of the planes to compute
     */
    public SimMetadata(
            @Element(name = "acquisitionConfig") AcquisitionConfig acqConfig,
            @Element(name = "scatteringFactors") ScatteringFactorsEnum scatteringFactors,
            @Element(name = "maxIndex") int maxIndex) {
        super(acqConfig);

        setScatteringFactors(scatteringFactors);
        setMaxIndex(maxIndex);
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



    /**
     * Returns the maximum index of the planes to compute in the simulation.
     * 
     * @return maximum index of the planes
     */
    public int getMaxIndex() {
        return maxIndex;
    }



    /**
     * Returns the type of scattering factors.
     * 
     * @return type of scattering factors.
     */
    public ScatteringFactorsEnum getScatteringFactors() {
        return scatteringFactors;
    }



    /**
     * Sets the maximum index of the planes to compute in the simulation.
     * 
     * @param maxIndex
     *            maximum index of the planes
     */
    public void setMaxIndex(int maxIndex) {
        if (maxIndex < 1)
            throw new IllegalArgumentException(
                    "The maximum index has to greater or equal to 1.");
        this.maxIndex = maxIndex;
    }



    /**
     * Sets the type of scattering factors.
     * 
     * @param scatteringFactors
     *            type of scattering factors.
     */
    public void setScatteringFactors(ScatteringFactorsEnum scatteringFactors) {
        if (scatteringFactors == null)
            throw new NullPointerException("Scattering factors cannot be null.");
        this.scatteringFactors = scatteringFactors;
    }

}
