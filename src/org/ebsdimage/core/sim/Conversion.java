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

import ptpshared.math.old.Matrix3D;
import ptpshared.math.old.Quaternion;

/**
 * Conversion of a rotation between different coordinates system.
 * 
 * @author Philippe T. Pinard
 */
public class Conversion {

    /** Matrix to convert to HKL coordinates system. */
    public static final Matrix3D HKL_EQUIVALENT_MATRIX = new Matrix3D(-1, 0, 0,
            0, 0, -1, 0, -1, 0);



    /**
     * Convert a rotation in the HKL Flamenco coordinates system to the
     * coordinates system used in this module. The detector orientation in HKL
     * Flamenco must be set to (0.0, 0.0, 0.0).
     * 
     * @param rotation
     *            rotation is reported in HKL Flamenco
     * @return equivalent rotation in the module's coordinates system
     */
    public static Quaternion fromHKL(Quaternion rotation) {
        return new Quaternion(HKL_EQUIVALENT_MATRIX).multiply(rotation);
    }

    // Future implementation
    // /**
    // * Returns a list of quaternions representing the rotation of a pattern.
    // It
    // * combines the crystal orientation, the specimen rotation and the tilt.
    // *
    // * @param crystal
    // * rotation of the crystal (reported rotation)
    // * @param specimen
    // * rotation of the specimen (in respect to the microscope) This
    // * should not take into account the tilt.
    // * @param tilt
    // * tilt angle
    // * @return list of quaternions
    // */
    // public static Quaternion[] getRotations(Quaternion crystal,
    // Quaternion specimen, double tilt) {
    // Quaternion qTilt = new Quaternion(new AxisAngle(-tilt, 1, 0, 0));
    // Quaternion qDetector = Quaternion.IDENTITY;
    //
    // return new Quaternion[] { specimen, crystal, qTilt, qDetector };
    // }
}
