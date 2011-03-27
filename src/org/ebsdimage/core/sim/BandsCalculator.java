package org.ebsdimage.core.sim;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.core.Microscope;

import crystallography.core.Reflectors;

/**
 * Calculates the representation of a crystal's reflectors in a diffraction
 * pattern.
 * 
 * @author Philippe T. Pinard
 */
public interface BandsCalculator {

    /**
     * Calculates all the bands from the specified reflectors in the given
     * experimental conditions.
     * 
     * @param width
     *            width of the diffraction pattern image
     * @param height
     *            height of the diffraction pattern image
     * @param microscope
     *            microscope configuration
     * @param reflectors
     *            list of reflectors
     * @param rotation
     *            rotation of the crystal
     * @return bands
     */
    public Band[] calculate(int width, int height, Microscope microscope,
            Reflectors reflectors, Rotation rotation);
}
