package org.ebsdimage.core.exp.ops.detection.results;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;

public class AreaTest extends TestCase {

    private Area area;

    private BinMap peaksMap;



    @Before
    public void setUp() throws Exception {
        area = new Area();

        peaksMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/peaksMap.bmp"));
    }



    @Test
    public void testToString() {
        assertEquals("Area", area.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = area.calculate(null, peaksMap);

        assertEquals(4, results.length);

        // Average
        assertEquals(67.25, results[0].value.doubleValue(), 1e-6);

        // Std Dev
        assertEquals(18.49131012, results[1].value.doubleValue(), 1e-6);

        // Min
        assertEquals(40.0, results[2].value.doubleValue(), 1e-6);

        // Max
        assertEquals(94.0, results[3].value.doubleValue(), 1e-6);
    }

}
