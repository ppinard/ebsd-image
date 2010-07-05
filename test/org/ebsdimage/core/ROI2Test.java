package org.ebsdimage.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;

import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;

public class ROI2Test {

    private ROI2 roi;



    @Before
    public void setUp() throws Exception {
        roi = new ROI2();
        roi.addPoint(1, 1);
        roi.addPoint(4, 1);
        roi.addPoint(3, 3);
        roi.addPoint(1, 3);
    }



    @Test
    public void testROI2() {
        ROI2 roi = new ROI2();
        assertTrue(roi.isEmpty());
    }



    @Test
    public void testROI2IntArrayIntArrayInt() {
        ROI2 roi = new ROI2(new int[] { 1, 3 }, new int[] { 3, 1 }, 2);

        Rectangle bounds = roi.getBounds();
        assertEquals(1, bounds.x);
        assertEquals(1, bounds.y);
        assertEquals(2, bounds.width);
        assertEquals(2, bounds.height);
    }



    @Test
    public void testROI2ROI2() {
        ROI2 roi = new ROI2(this.roi);

        assertTrue(roi != this.roi);

        for (int n = 0; n < roi.npoints; n++) {
            assertEquals(roi.xpoints[n], this.roi.xpoints[n]);
            assertEquals(roi.ypoints[n], this.roi.ypoints[n]);
        }
    }



    @Test
    public void testContainsMap() {
        BinMap map = new BinMap(3, 2);

        assertFalse(roi.contains(map));
    }



    @Test
    public void testContainsMap2() {
        BinMap map = new BinMap(4, 3);

        assertTrue(roi.contains(map));
    }



    @Test
    public void testContainsMap3() {
        BinMap map = new BinMap(10, 10);

        assertTrue(roi.contains(map));
    }



    @Test
    public void testGetExtentLabel() {
        String expected = "(1;1)->(3;2)";
        assertEquals(expected, roi.getExtentLabel());
    }



    @Test
    public void testIntersectsMap() {
        BinMap map = new BinMap(3, 2);

        assertTrue(roi.intersects(map));
    }



    @Test
    public void testIntersectsMap2() {
        BinMap map = new BinMap(4, 3);

        assertTrue(roi.intersects(map));
    }



    @Test
    public void testIntersectsMap3() {
        BinMap map = new BinMap(10, 10);

        assertTrue(roi.intersects(map));
    }



    @Test
    public void testIsEmpty() {
        assertFalse(roi.isEmpty());
        assertTrue(new ROI2().isEmpty());
    }



    @Test
    public void testGetBounds() {
        Rectangle bounds = roi.getBounds();

        assertEquals(1, bounds.x);
        assertEquals(1, bounds.y);
        assertEquals(3, bounds.width);
        assertEquals(2, bounds.height);
    }

}
