package org.ebsdimage.core.exp.ops.pattern.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;

public class SumRegionTest extends TestCase {

    private SumRegion averageRegion;

    private ByteMap patternMap;



    @Before
    public void setUp() throws Exception {
        averageRegion = new SumRegion(0.2, 0.3, 0.5, 0.6);
        patternMap =
                (ByteMap) load(getFile("org/ebsdimage/testdata/pattern.bmp"));
    }



    @Test
    public void testCalculateByteMap() {
        OpResult[] results = averageRegion.calculate(patternMap);

        assertEquals(1, results.length);
        assertEquals(820193, results[0].value.doubleValue(), 1e-6);
    }



    @Test
    public void testAverageRegionDoubleDoubleDoubleDouble() {
        assertEquals(0.2, averageRegion.xmin, 1e-6);
        assertEquals(0.3, averageRegion.ymin, 1e-6);
        assertEquals(0.5, averageRegion.xmax, 1e-6);
        assertEquals(0.6, averageRegion.ymax, 1e-6);
    }



    @Test
    public void testAverageRegion() {
        SumRegion other = new SumRegion();
        assertEquals(SumRegion.DEFAULT_XMIN, other.xmin, 1e-6);
        assertEquals(SumRegion.DEFAULT_YMIN, other.ymin, 1e-6);
        assertEquals(SumRegion.DEFAULT_XMAX, other.xmax, 1e-6);
        assertEquals(SumRegion.DEFAULT_YMAX, other.ymax, 1e-6);
    }



    @Test
    public void testEqualsObject() {
        SumRegion other = new SumRegion(0.2, 0.3, 0.5, 0.6);

        assertFalse(other == averageRegion);
        assertTrue(other.equals(averageRegion));

        assertFalse(new SumRegion(0.2, 0.3, 0.5, 0.9).equals(averageRegion));
    }



    @Test
    public void testToString() {
        String expected =
                "AverageRegion [xmin=0.2, ymin=0.3, xmax=0.5, ymax=0.6]";
        assertEquals(expected, averageRegion.toString());
    }

}
