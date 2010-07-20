package org.ebsdimage.core.exp.ops.identification.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

public class StandardDeviationTest {

    private StandardDeviation stdDev;

    private HoughPeak[] peaks;



    @Before
    public void setUp() throws Exception {
        stdDev = new StandardDeviation(2);
        peaks =
                new HoughPeak[] { new HoughPeak(3.0, 0.5, 1),
                        new HoughPeak(5.0, 1.5, 2), new HoughPeak(7.0, 2.5, 4) };
    }



    @Test
    public void testEqualsObject() {
        StandardDeviation other = new StandardDeviation(2);

        assertFalse(other == stdDev);
        assertTrue(other.equals(stdDev));

        assertFalse(new StandardDeviation(3).equals(stdDev));
    }



    @Test
    public void testToString() {
        String expected = "Standard Deviation [max=2]";
        assertEquals(expected, stdDev.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = stdDev.calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(1.41421356, results[0].value.doubleValue(), 1e-6);

        results = new StandardDeviation().calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(1.52752523, results[0].value.doubleValue(), 1e-6);
    }



    @Test
    public void testStandardDeviation() {
        StandardDeviation other = new StandardDeviation();
        assertEquals(StandardDeviation.DEFAULT_MAX, other.max);
    }



    @Test
    public void testStandardDeviationInt() {
        assertEquals(2, stdDev.max);
    }

}
