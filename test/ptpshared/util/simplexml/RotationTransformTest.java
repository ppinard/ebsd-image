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

import org.apache.commons.math.geometry.Rotation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RotationTransformTest {

    private RotationTransform transform;



    @Before
    public void setUp() throws Exception {
        transform = new RotationTransform();
    }



    @Test
    public void testRead() throws Exception {
        String value = "1.0;2.0;3.0;4.0";

        Rotation expected = new Rotation(1.0, 2.0, 3.0, 4.0, false);
        Rotation actual = transform.read(value);

        assertEquals(expected.getQ0(), actual.getQ0(), 1e-6);
        assertEquals(expected.getQ1(), actual.getQ1(), 1e-6);
        assertEquals(expected.getQ2(), actual.getQ2(), 1e-6);
        assertEquals(expected.getQ3(), actual.getQ3(), 1e-6);
    }



    @Test
    public void testWrite() throws Exception {
        Rotation r = new Rotation(1.0, 2.0, 3.0, 4.0, false);

        String expected = "1.0;2.0;3.0;4.0";
        String actual = transform.write(r);

        assertEquals(expected, actual);
    }

}
