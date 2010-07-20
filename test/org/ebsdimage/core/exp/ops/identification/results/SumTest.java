package org.ebsdimage.core.exp.ops.identification.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

public class SumTest {

    private Sum sum;

    private HoughPeak[] peaks;



    @Before
    public void setUp() throws Exception {
        sum = new Sum(2);
        peaks =
                new HoughPeak[] { new HoughPeak(3.0, 0.5, 1),
                        new HoughPeak(5.0, 1.5, 2), new HoughPeak(7.0, 2.5, 4) };
    }



    @Test
    public void testEqualsObject() {
        Sum other = new Sum(2);

        assertFalse(other == sum);
        assertTrue(other.equals(sum));

        assertFalse(new Sum(3).equals(sum));
    }



    @Test
    public void testToString() {
        String expected = "Sum [max=2]";
        assertEquals(expected, sum.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = sum.calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(6.0, results[0].value.doubleValue(), 1e-6);

        results = new Sum().calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(7.0, results[0].value.doubleValue(), 1e-6);
    }



    @Test
    public void testSum() {
        Sum other = new Sum();
        assertEquals(Sum.DEFAULT_MAX, other.max);
    }



    @Test
    public void testSumInt() {
        assertEquals(2, sum.max);
    }

}
