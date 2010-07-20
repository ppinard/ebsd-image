package org.ebsdimage.core.exp.ops.detection.results;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;

public class CountTest extends TestCase {

    private Count count;

    private BinMap peaksMap;



    @Before
    public void setUp() throws Exception {
        count = new Count();

        peaksMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/peaksMap.bmp"));
    }



    @Test
    public void testToString() {
        assertEquals("Count", count.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = count.calculate(null, peaksMap);

        assertEquals(1, results.length);
        assertEquals(8, results[0].value.intValue());
    }

}
