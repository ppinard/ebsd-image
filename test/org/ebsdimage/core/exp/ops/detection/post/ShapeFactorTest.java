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
package org.ebsdimage.core.exp.ops.detection.post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;
import rmlimage.core.Identification;

public class ShapeFactorTest extends TestCase {

    private ShapeFactor shapeFactor;

    private BinMap srcMap;



    @Before
    public void setUp() throws Exception {
        shapeFactor = new ShapeFactor(1.5);
        srcMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/automatic_top_hat.bmp"));
    }



    @Test
    public void testEqualsObject() {
        ShapeFactor same = new ShapeFactor(1.5);
        ShapeFactor diff = new ShapeFactor(3.0);

        assertFalse(same == shapeFactor);
        assertTrue(same.equals(shapeFactor));
        assertFalse(diff.equals(shapeFactor));
    }



    @Test
    public void testToString() {
        String expected = "ShapeFactor [aspectRatio=1.5]";
        assertEquals(expected, shapeFactor.toString());
    }



    @Test
    public void testProcess() throws Exception {
        assertEquals(25, Identification.identify(srcMap).getObjectCount());

        BinMap destMap = shapeFactor.process(null, srcMap);
        assertEquals(7, Identification.identify(destMap).getObjectCount());
    }



    @Test
    public void testShapeFactor() {
        ShapeFactor other = new ShapeFactor();
        assertEquals(ShapeFactor.DEFAULT_ASPECT_RATIO, other.aspectRatio, 1e-6);
    }



    @Test
    public void testShapeFactorDouble() {
        assertEquals(1.5, shapeFactor.aspectRatio, 1e-6);
    }

}
