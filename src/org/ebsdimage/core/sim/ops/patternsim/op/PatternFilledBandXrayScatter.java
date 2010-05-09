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
package org.ebsdimage.core.sim.ops.patternsim.op;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.PatternFilledBand;
import org.ebsdimage.core.sim.Sim;

import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;
import crystallography.core.Reflectors;
import crystallography.core.XrayScatteringFactors;

/**
 * Operation to draw a filled band pattern using x-rays scattering factors.
 * 
 * @author Philippe T. Pinard
 */
public class PatternFilledBandXrayScatter extends PatternSimOp {

    /** Width of the pattern. */
    public final int width;

    /** Default width of the pattern. */
    public static final int DEFAULT_WIDTH = 1344;

    /** Height of the pattern. */
    public final int height;

    /** Default height of the pattern. */
    public static final int DEFAULT_HEIGHT = 1024;

    /** Maximum reflector index. */
    public final int maxIndex;

    /** Default maximum reflector index. */
    public static final int DEFAULT_MAX_INDEX = 6;



    /**
     * Creates a new <code>FilledBandXrayScatter</code> using the default width
     * and height.
     */
    public PatternFilledBandXrayScatter() {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        maxIndex = DEFAULT_MAX_INDEX;
    }



    /**
     * Creates a new <code>FilledBandXrayScatter</code> using the specified
     * width and height.
     * 
     * @param width
     *            width of the pattern
     * @param height
     *            height of the pattern
     * @param maxIndex
     *            maximum index of the planes to compute
     */
    public PatternFilledBandXrayScatter(int width, int height, int maxIndex) {
        this.width = width;
        this.height = height;
        this.maxIndex = maxIndex;
    }



    /**
     * Returns the drawn pattern from the specified parameters. The x-rays
     * scattering factors are calculated from the <code>Crystal</code>.
     * 
     * @param sim
     *            simulation executing this method
     * @param crystal
     *            unit cell and atom sites
     * @param camera
     *            parameters of the camera
     * @param energy
     *            beam energy (in eV)
     * @param rotation
     *            rotation of the pattern
     */
    @Override
    public void simulate(Sim sim, Camera camera, Crystal crystal,
            Energy energy, Quaternion rotation) {
        XrayScatteringFactors scatter = new XrayScatteringFactors();
        Reflectors refls = new Reflectors(crystal, scatter, maxIndex);

        PatternFilledBand pattern = new PatternFilledBand(width, height, refls,
                -1, camera, energy, rotation);

        this.pattern = pattern;
    }



    /**
     * Returns a representation of this
     * <code>PatternFilledBandXrayScatter</code>, suitable for debugging.
     * 
     * @return information about this <code>PatternFilledBandXrayScatter</code>
     */
    @Override
    public String toString() {
        return "PatternFilledBandXrayScatter [width=" + width + ", height="
                + height + ", maxIndex=" + maxIndex + "]";
    }

}
