package org.ebsdimage.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CentroidTest {

    private Centroid centroid;



    @Before
    public void setUp() throws Exception {
        centroid = new Centroid(1);
        centroid.x[0] = 1.0;
        centroid.y[0] = 2.0;
        centroid.intensity[0] = 3.0;
    }



    @Test
    public void testCentroidInt() {
        assertEquals(3, centroid.getFieldCount());
        assertEquals(1, centroid.getValueCount());
        assertEquals("rad", centroid.units[0]);
        assertEquals("px", centroid.units[1]);
        assertEquals("", centroid.units[2]);

        assertEquals(1.0, centroid.x[0], 1e-6);
        assertEquals(2.0, centroid.y[0], 1e-6);
        assertEquals(3.0, centroid.intensity[0], 1e-6);
    }



    @Test
    public void testCentroidIntString() {
        Centroid centroid = new Centroid(1, "m");

        assertEquals(3, centroid.getFieldCount());
        assertEquals(1, centroid.getValueCount());
        assertEquals("rad", centroid.units[0]);
        assertEquals("m", centroid.units[1]);
        assertEquals("", centroid.units[2]);
    }



    @Test
    public void testDuplicate() {
        Centroid dup = centroid.duplicate();

        assertEquals(3, dup.getFieldCount());
        assertEquals(1, dup.getValueCount());
        assertEquals("rad", dup.units[0]);
        assertEquals("px", dup.units[1]);
        assertEquals("", dup.units[2]);

        assertEquals(1.0, dup.x[0], 1e-6);
        assertEquals(2.0, dup.y[0], 1e-6);
        assertEquals(3.0, dup.intensity[0], 1e-6);
    }



    @Test
    public void testToHoughPeakArray() {
        HoughPeak[] peaks = centroid.toHoughPeakArray();

        assertEquals(1, peaks.length);
        assertEquals(1.0, peaks[0].theta, 1e-6);
        assertEquals(2.0, peaks[0].rho, 1e-6);
        assertEquals("px", peaks[0].rhoUnits);
        assertEquals(3.0, peaks[0].intensity, 1e-6);
    }

}
