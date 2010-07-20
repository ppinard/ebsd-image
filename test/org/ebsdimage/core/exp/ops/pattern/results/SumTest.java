package org.ebsdimage.core.exp.ops.pattern.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;

public class SumTest extends TestCase {

    private Sum sum;

    private ByteMap patternMap;



    @Before
    public void setUp() throws Exception {
        sum = new Sum(0.2, 0.3, 0.5, 0.6);
        patternMap =
                (ByteMap) load(getFile("org/ebsdimage/testdata/pattern.bmp"));
    }



    @Test
    public void testCalculateByteMap() {
        double value = sum.calculateSum(patternMap);
        assertEquals(820193, value, 1e-6);
    }



    @Test
    public void testAverageRegionDoubleDoubleDoubleDouble() {
        assertEquals(0.2, sum.xmin, 1e-6);
        assertEquals(0.3, sum.ymin, 1e-6);
        assertEquals(0.5, sum.xmax, 1e-6);
        assertEquals(0.6, sum.ymax, 1e-6);
    }



    @Test
    public void testAverageRegion() {
        Sum other = new Sum();
        assertEquals(Sum.DEFAULT_XMIN, other.xmin, 1e-6);
        assertEquals(Sum.DEFAULT_YMIN, other.ymin, 1e-6);
        assertEquals(Sum.DEFAULT_XMAX, other.xmax, 1e-6);
        assertEquals(Sum.DEFAULT_YMAX, other.ymax, 1e-6);
    }



    @Test
    public void testEqualsObject() {
        Sum other = new Sum(0.2, 0.3, 0.5, 0.6);

        assertFalse(other == sum);
        assertTrue(other.equals(sum));

        assertFalse(new Sum(0.2, 0.3, 0.5, 0.9).equals(sum));
    }



    @Test
    public void testToString() {
        String expected = "Sum [xmin=0.2, ymin=0.3, xmax=0.5, ymax=0.6]";
        assertEquals(expected, sum.toString());
    }

}
