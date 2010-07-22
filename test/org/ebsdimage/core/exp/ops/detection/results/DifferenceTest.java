package org.ebsdimage.core.exp.ops.detection.results;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.OpResult;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;

public class DifferenceTest extends TestCase {

    private Difference difference;

    private BinMap peaksMap;

    private HoughMap houghMap;



    @Before
    public void setUp() throws Exception {
        difference = new Difference();

        peaksMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/automatic_top_hat.bmp"));
        houghMap =
                new HoughMapLoader().load(getFile("org/ebsdimage/testdata/houghmap_cropped.bmp"));
    }



    @Test
    public void testToString() {
        assertEquals("Difference", difference.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = difference.calculate(peaksMap, houghMap);

        assertEquals(4, results.length);

        // Average
        assertEquals(6.68, results[0].value.doubleValue(), 1e-6);

        // Std Dev
        assertEquals(8.61065231, results[1].value.doubleValue(), 1e-6);

        // Min
        assertEquals(0.0, results[2].value.doubleValue(), 1e-6);

        // Max
        assertEquals(26.0, results[3].value.doubleValue(), 1e-6);
    }

}
