package org.ebsdimage.core.exp.ops.identification.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

public class AverageTest {

    private Average average;

    private HoughPeak[] peaks;



    @Before
    public void setUp() throws Exception {
        average = new Average(2);
        peaks =
                new HoughPeak[] { new HoughPeak(3.0, 0.5, 1),
                        new HoughPeak(5.0, 1.5, 2), new HoughPeak(7.0, 2.5, 4) };
    }



    @Test
    public void testEqualsObject() {
        Average other = new Average(2);

        assertFalse(other == average);
        assertTrue(other.equals(average));

        assertFalse(new Average(3).equals(average));
    }



    @Test
    public void testToString() {
        String expected = "Average [max=2]";
        assertEquals(expected, average.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = average.calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(3.0, results[0].value.doubleValue(), 1e-6);

        results = new Average().calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(7.0 / 3.0, results[0].value.doubleValue(), 1e-6);
    }



    @Test
    public void testAverage() {
        Average other = new Average();
        assertEquals(Average.DEFAULT_MAX, other.max);
    }



    @Test
    public void testAverageInt() {
        assertEquals(2, average.max);
    }

}
