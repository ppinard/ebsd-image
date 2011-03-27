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

import org.apache.commons.math.geometry.Vector3D;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Line3DTest {

    private Vector3D vector;

    private Vector3D point;

    private Line3D line;



    @Before
    public void setUp() throws Exception {
        vector = new Vector3D(1, 2, 3);
        point = new Vector3D(4, 5, 6);
        line = new Line3D(point, vector);
    }



    @Test
    public void testParametrizedLine() {
        assertEquals(vector, line.v);
        assertEquals(point, line.p);
    }



    @Test
    public void testToString() {
        String expected = "{4; 5; 6} + {1; 2; 3}t";
        assertEquals(expected, line.toString());
    }

}
