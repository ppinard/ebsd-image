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
package ptpshared.util.simplexml;

import org.apache.commons.math.geometry.Vector3D;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Vector3DTransformTest {

    private Vector3DTransform transform;



    @Before
    public void setUp() throws Exception {
        transform = new Vector3DTransform();
    }



    @Test
    public void testRead() throws Exception {
        String value = "1.0;2.0;3.0";

        Vector3D expected = new Vector3D(1.0, 2.0, 3.0);
        Vector3D actual = transform.read(value);

        assertEquals(expected.getX(), actual.getX(), 1e-6);
        assertEquals(expected.getY(), actual.getY(), 1e-6);
        assertEquals(expected.getZ(), actual.getZ(), 1e-6);
    }



    @Test
    public void testWrite() throws Exception {
        Vector3D v = new Vector3D(1.0, 2.0, 3.0);

        String expected = "1.0;2.0;3.0";
        String actual = transform.write(v);

        assertEquals(expected, actual);
    }
}
