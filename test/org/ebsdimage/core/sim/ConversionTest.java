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

import static java.lang.Math.PI;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ptpshared.math.old.Eulers;
import ptpshared.math.old.Quaternion;
import ptpshared.math.old.Vector3D;

public class ConversionTest {

    @Test
    public void testFromHKL() {
        Quaternion before = new Quaternion(new Eulers(PI / 2, 0.0, 0.0));
        Quaternion after = Conversion.fromHKL(before);

        assertTrue(after.toEuler().equals(new Eulers(0.0, PI / 2, -PI / 2),
                1e-6));
    }



    @Test
    public void testHKL_EQUIVALENT_MATRIX() {
        Vector3D before;
        Vector3D after;

        before = new Vector3D(1, 1, 1);
        after = Conversion.HKL_EQUIVALENT_MATRIX.multiply(before);
        assertTrue(after.equals(new Vector3D(-1.0, -1.0, -1.0), 1e-6));

        before = new Vector3D(-1, 1, 1);
        after = Conversion.HKL_EQUIVALENT_MATRIX.multiply(before);
        assertTrue(after.equals(new Vector3D(1.0, -1.0, -1.0), 1e-6));

        before = new Vector3D(-1, -1, 1);
        after = Conversion.HKL_EQUIVALENT_MATRIX.multiply(before);
        assertTrue(after.equals(new Vector3D(1.0, -1.0, 1.0), 1e-6));
    }

}
