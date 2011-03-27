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

import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Point2DUtilsTest {

    private Point2D p;



    @Before
    public void setUp() throws Exception {
        p = new Point2D.Double(1, 2);
    }



    @Test
    public void testEqualsPoint2DPoint2DDouble() {
        assertFalse(Point2DUtils.equals(p, null, 1e-6));
        assertFalse(Point2DUtils.equals(null, p, 1e-6));

        Point2D other = new Point2D.Double(1.001, 2.001);
        assertTrue(Point2DUtils.equals(p, other, 1e-3));

        other = new Point2D.Double(1.1, 2.001);
        assertFalse(Point2DUtils.equals(p, other, 1e-3));

        other = new Point2D.Double(1.001, 2.1);
        assertFalse(Point2DUtils.equals(p, other, 1e-3));
    }



    @Test
    public void testToStringPoint2D() {
        String expected = "(1.0;2.0)";
        assertEquals(expected, Point2DUtils.toString(p));

    }

}
