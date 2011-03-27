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
package crystallography.core;

import java.util.HashMap;

/**
 * Abstract class for the scattering factors.
 * 
 * @author Philippe T. Pinard
 */
public abstract class ScatteringFactors {

    /** Store coefficients for <code>s=0</code> to <code>s=2</code>. */
    public final HashMap<Integer, HashMap<String, Double>> coefficients02;

    /** Store coefficients for <code>s=2</code> to <code>s=6</code>. */
    public final HashMap<Integer, HashMap<String, Double>> coefficients26;



    /**
     * Creates a new <code>ScatteringFactors</code>.
     */
    public ScatteringFactors() {
        coefficients02 = new HashMap<Integer, HashMap<String, Double>>();
        coefficients26 = new HashMap<Integer, HashMap<String, Double>>();
    }



    /**
     * Returns the scattering factor for the given atomic number and plane
     * spacing.
     * 
     * @param atomicNumber
     *            atomic number
     * @param planeSpacing
     *            plane spacing
     * @return scattering factor
     */
    public abstract double getFromPlaneSpacing(int atomicNumber,
            double planeSpacing);



    /**
     * Returns the scattering factor from the fitting variable <code>s</code>.
     * The equation for <code>s</code> varies between different types of
     * scattering factors.
     * 
     * @param atomicNumber
     *            atomic number
     * @param s
     *            fitting variable
     * @return scattering factor
     */
    public abstract double getFromS(int atomicNumber, double s);

}
