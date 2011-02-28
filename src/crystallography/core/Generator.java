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
package crystallography.core;

import net.jcip.annotations.Immutable;

import org.apache.commons.math.geometry.Vector3D;

/**
 * Space group's generator to calculate equivalent positions.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class Generator {

    /** Rotation matrix. */
    public final double[][] m;

    /** Translation vector. */
    public final double[] t;



    /**
     * Creates a new <code>Generator</code>.
     * 
     * @param m
     *            rotation matrix
     * @param t
     *            translation vector
     */
    public Generator(double[][] m, double[] t) {
        // dimension check
        if ((m.length != 3) || (m[0].length != 3) || (m[1].length != 3)
                || (m[2].length != 3))
            throw new IllegalArgumentException(
                    "The matrix must be a 3x3 matrix.");
        if (t.length != 3)
            throw new IllegalArgumentException("The translation array ("
                    + t.length + ") must have a length of 3.");

        this.m = m;
        this.t = t;
    }



    /**
     * Apply the generator on an atom position.
     * 
     * @param atom
     *            an atom
     * @return resultant atom after applying the generator
     */
    public AtomSite apply(AtomSite atom) {
        double px = atom.position.getX();
        double py = atom.position.getY();
        double pz = atom.position.getZ();

        double x = m[0][0] * px + m[0][1] * py + m[0][2] * pz + t[0];
        double y = m[1][0] * px + m[1][1] * py + m[1][2] * pz + t[1];
        double z = m[2][0] * px + m[2][1] * py + m[2][2] * pz + t[2];

        return new AtomSite(atom.atomicNumber, new Vector3D(x, y, z));
    }

}
