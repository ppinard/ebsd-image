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
package ptpshared.core.math;

import java.io.File;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static java.lang.Math.sqrt;

import static junittools.test.Assert.assertEquals;

public class AxisAngleTest extends TestCase {

    private AxisAngle axisAngle1;

    private AxisAngle axisAngle2;



    @Before
    public void setUp() throws Exception {
        axisAngle1 = new AxisAngle(0.5, new Vector3D(1, 2, 3));
        axisAngle2 = new AxisAngle(0.5, 1, 2, 3);
    }



    @Test
    public void testAxisAngleDoubleDoubleDoubleDouble() {
        assertEquals(0.5, axisAngle2.angle, 1e-7);
        assertEquals(1.0 / sqrt(14), axisAngle2.axis.v[0], 1e-7);
        assertEquals(2 / sqrt(14), axisAngle2.axis.v[1], 1e-7);
        assertEquals(3 / sqrt(14), axisAngle2.axis.v[2], 1e-7);
    }



    @Test
    public void testAxisAngleDoubleVector3D() {
        assertEquals(0.5, axisAngle1.angle, 1e-7);
        assertEquals(1 / sqrt(14), axisAngle1.axis.v[0], 1e-7);
        assertEquals(2 / sqrt(14), axisAngle1.axis.v[1], 1e-7);
        assertEquals(3 / sqrt(14), axisAngle1.axis.v[2], 1e-7);
    }



    @Test(expected = NullPointerException.class)
    public void testAxisAngleDoubleVector3DException1() {
        new AxisAngle(0.5, null);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAxisAngleDoubleVector3DException2() {
        new AxisAngle(0.5, new Vector3D(0, 0, 0));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAxisAngleDoubleVector3DException3() {
        new AxisAngle(99, new Vector3D(1, 2, 3));
    }



    @Test
    public void testClone() {
        AxisAngle duplicate = axisAngle1.clone();
        assertEquals(duplicate.angle, axisAngle1.angle, 1e-7);
        assertEquals(duplicate.axis.v[0], axisAngle1.axis.v[0], 1e-7);
        assertEquals(duplicate.axis.v[1], axisAngle1.axis.v[1], 1e-7);
        assertEquals(duplicate.axis.v[2], axisAngle1.axis.v[2], 1e-7);
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(axisAngle1.equals(axisAngle1, 1e-6));

        assertFalse(axisAngle1.equals(null, 1e-6));

        assertFalse(axisAngle1.equals(new AxisAngle(0.5, 1, 2, 3.000001), 1e-7));

        assertFalse(axisAngle1.equals(new AxisAngle(0.50001, 1, 2, 3), 1e-6));

        assertTrue(axisAngle1.equals(new AxisAngle(0.5, 1, 2, 3.000001), 1e-6));
    }



    @Test
    public void testEqualsObject() {
        assertTrue(axisAngle1.equals(axisAngle1));

        assertFalse(axisAngle1.equals(null));

        assertFalse(axisAngle1.equals(new Object()));

        assertTrue(axisAngle1.equals(axisAngle2));

        assertFalse(new AxisAngle(1.5, 1, 2, 3).equals(axisAngle1));

        assertFalse(new AxisAngle(0.5, 99, 2, 3).equals(axisAngle1));

        assertTrue(new AxisAngle(0.5, 1, 2, 3).equals(axisAngle1));
    }



    @Test
    public void testHashCode() {
        assertEquals(new AxisAngle(0.5, 1, 2, 3).hashCode(),
                axisAngle1.hashCode());
    }



    @Test
    public void testToString() {
        String expected =
                "AxisAngle [angle=28.64788975654116, axis=(0.2672612419124244;0.5345224838248488;0.8017837257372732)]";
        assertEquals(expected, axisAngle1.toString());
    }



    @Test
    public void testXML() throws Exception {
        File tmpFile = createTempFile();

        // Write
        new XmlSaver().save(axisAngle1, tmpFile);

        // Read
        AxisAngle other = new XmlLoader().load(AxisAngle.class, tmpFile);
        assertEquals(axisAngle1, other, 1e-6);
    }
}
