package org.ebsdimage.core.exp.ops.detection.results;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;

public class DiameterTest extends TestCase {

    private Diameter diameter;

    private BinMap peaksMap;



    @Before
    public void setUp() throws Exception {
        diameter = new Diameter();

        peaksMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/peaksMap.bmp"));
    }



    @Test
    public void testToString() {
        assertEquals("Diameter", diameter.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = diameter.calculate(null, peaksMap);

        assertEquals(4, results.length);

        // Average
        assertEquals(13.57892894, results[0].value.doubleValue(), 1e-6);

        // Std Dev
        assertEquals(2.361767530, results[1].value.doubleValue(), 1e-6);

        // Min
        assertEquals(9.245048523, results[2].value.doubleValue(), 1e-6);

        // Max
        assertEquals(15.55825805, results[3].value.doubleValue(), 1e-6);
    }

}
