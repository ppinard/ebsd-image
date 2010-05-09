/*
 * EBSD-Image
 * Copyright (C) 2010 Philippe T. Pinard
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

import static java.lang.Math.min;

import java.util.ArrayList;

import org.ebsdimage.core.Camera;

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import ptpshared.utility.xml.ObjectXml;
import rmlimage.core.ByteMap;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Reflector;
import crystallography.core.Reflectors;

/**
 * Superclass for pattern drawing objects.
 * 
 * @author Philippe T. Pinard
 */
public abstract class Pattern implements ObjectXml {

    /** Width of the pattern. */
    public final int width;

    /** Height of the pattern. */
    public final int height;

    /** List of reflectors. */
    public final Reflectors reflectors;

    /** Number of reflectors to draw. */
    public final int numberReflectors;

    /** EBSD camera parameters. */
    public final Camera camera;

    /** Beam energy. */
    public final Energy energy;

    /** Rotation of the pattern. */
    public final Quaternion rotation;

    /** List of bands. */
    protected ArrayList<Band> bands;

    /** Pattern's <code>ByteMap</code>. */
    protected ByteMap patternMap;

    /** Pattern's <code>RealMap</code>. */
    protected RealMap patternRealMap;



    /**
     * Creates a new pattern with the specified dimensions and parameters. If
     * the number of reflectors is negative, all the reflectors are taken.
     * 
     * @param width
     *            width of the pattern
     * @param height
     *            height of the pattern
     * @param reflectors
     *            of the pattern
     * @param numberReflectors
     *            number of reflectors to consider
     * @param camera
     *            parameters of the camera
     * @param energy
     *            beam energy (in eV)
     * @param rotation
     *            rotation of the pattern
     * 
     * @throws NullPointerException
     *             if the reflectors are null
     * @throws NullPointerException
     *             if the camera are null
     * @throws NullPointerException
     *             if the energy are null
     * @throws NullPointerException
     *             if the rotation are null
     */
    public Pattern(int width, int height, Reflectors reflectors,
            int numberReflectors, Camera camera, Energy energy, Eulers rotation) {
        this(width, height, reflectors, numberReflectors, camera, energy,
                new Quaternion(rotation));
    }



    /**
     * Creates a new pattern with the specified dimensions and parameters. If
     * the number of reflectors is negative, all the reflectors are taken.
     * 
     * @param width
     *            width of the pattern
     * @param height
     *            height of the pattern
     * @param reflectors
     *            of the pattern
     * @param numberReflectors
     *            number of reflectors to consider
     * @param camera
     *            parameters of the camera
     * @param energy
     *            beam energy (in eV)
     * @param rotation
     *            rotation of the pattern
     * 
     * @throws NullPointerException
     *             if the reflectors are null
     * @throws NullPointerException
     *             if the camera are null
     * @throws NullPointerException
     *             if the energy are null
     * @throws NullPointerException
     *             if the rotation are null
     */
    public Pattern(int width, int height, Reflectors reflectors,
            int numberReflectors, Camera camera, Energy energy,
            Quaternion rotation) {
        if (reflectors == null)
            throw new NullPointerException("Reflectors cannot be null.");
        if (camera == null)
            throw new NullPointerException("Canera cannot be null.");
        if (energy == null)
            throw new NullPointerException("Energy cannot be null.");
        if (rotation == null)
            throw new NullPointerException("Rotation cannot be null.");

        this.width = width;
        this.height = height;
        this.reflectors = reflectors;
        this.camera = camera;
        this.energy = energy;
        this.rotation = rotation;

        // Automatically assigns the number of reflectors
        // as the maximum possible number.
        if (numberReflectors < 0)
            this.numberReflectors = reflectors.size();
        else
            this.numberReflectors = numberReflectors;
    }



    /**
     * Calculates the bands with the given parameters.
     */
    protected void calculateBands() {
        // Clear bands previously created.
        bands = new ArrayList<Band>();

        // Sort reflectors by decreasing order of intensity.
        reflectors.sortByIntensity(true);

        // Create bands
        for (int i = 0; i < min(numberReflectors, reflectors.size()); i++) {
            Reflector reflector = reflectors.get(i);

            // Create band and calculate half-widths, edge intercepts and full
            // width
            Band band;
            try {
                band = new Band(reflector, rotation, camera, energy.value);
            } catch (BandException e) {
                continue;
            }

            // Add band to Array
            bands.add(band);
        }
    }



    /**
     * Draws the pattern.
     */
    public abstract void draw();



    /**
     * Returns the calculated bands. If they are not previously calculated, they
     * are automatically calculated.
     * 
     * @return calculated bands
     */
    public Band[] getBands() {
        if (bands == null)
            calculateBands();
        return bands.toArray(new Band[bands.size()]);
    }



    /**
     * Returns the simulated pattern <code>ByteMap</code>. If the pattern was
     * not generated, it is automatically generated.
     * 
     * @return simulated pattern
     */
    public ByteMap getPatternMap() {
        if (patternMap == null)
            draw();

        return patternMap;
    }



    /**
     * Returns the simulated pattern <code>RealMap</code>. If the pattern was
     * not generated, it is automatically generated.
     * 
     * @return simulated pattern
     */
    public RealMap getPatternRealMap() {
        if (patternRealMap == null)
            draw();

        return patternRealMap;
    }

}
