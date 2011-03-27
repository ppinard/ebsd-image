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
package ptpshared.geom;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RotationUtilsTest {

    private Rotation r;



    @Before
    public void setUp() throws Exception {
        r = new Rotation(new Vector3D(1, 0, 0), Math.PI / 3);
    }



    @Test
    public void testEqualsRotationRotationDouble() {
        assertFalse(RotationUtils.equals(r, null, 1e-6));
        assertFalse(RotationUtils.equals(null, r, 1e-6));

        Rotation other = new Rotation(0.8660254037844387, -0.5, 0, 0, false);
        assertTrue(RotationUtils.equals(r, other, 1e-6));

        other = new Rotation(new Vector3D(-1, 0, 0), Math.PI / 3);
        assertFalse(RotationUtils.equals(r, other, 1e-6));

        other = new Rotation(new Vector3D(1, 0, 0), -Math.PI / 3);
        assertFalse(RotationUtils.equals(r, other, 1e-6));

        other = new Rotation(new Vector3D(-1, 0, 0), -Math.PI / 3);
        assertTrue(RotationUtils.equals(r, other, 1e-6));
    }



    @Test
    public void testToString() {
        String expected =
                "[[0.8660254037844387; -0.49999999999999994; -0.0; -0.0]]";
        assertEquals(expected, RotationUtils.toString(r));
    }

}
