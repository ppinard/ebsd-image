package org.ebsdimage.core.exp.ops.detection.pre;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.ebsdimage.core.HoughMap;
import org.junit.Before;
import org.junit.Test;

public class ThetaExpandTest {

    private ThetaExpand thetaExpand;

    private HoughMap houghMap;



    @Before
    public void setUp() throws Exception {
        thetaExpand = new ThetaExpand(1);
        houghMap = new HoughMap(3, 3, 1, 1);
        houghMap.pixArray[0] = 1;
        houghMap.pixArray[1] = 2;
        houghMap.pixArray[2] = 3;
        houghMap.pixArray[3] = 4;
        houghMap.pixArray[4] = 5;
        houghMap.pixArray[5] = 6;
        houghMap.pixArray[6] = 7;
        houghMap.pixArray[7] = 8;
        houghMap.pixArray[8] = 9;
    }



    @Test
    public void testProcess() throws IOException {
        HoughMap destMap = thetaExpand.process(null, houghMap);

        assertEquals(4, destMap.width);
        assertEquals(3, destMap.height);
    }



    @Test
    public void testThetaExpand() {
        ThetaExpand other = new ThetaExpand();
        assertEquals(ThetaExpand.DEFAULT_INCREMENT, other.increment, 1e-7);
    }



    @Test
    public void testThetaExpandInt() {
        assertEquals(1, thetaExpand.increment, 1e-7);
    }



    @Test
    public void testEqualsObject() {
        ThetaExpand other = new ThetaExpand(1);

        assertFalse(other == thetaExpand);
        assertTrue(other.equals(thetaExpand));

        assertFalse(new ThetaExpand(2).equals(thetaExpand));
    }



    @Test
    public void testToString() {
        String expected = "ThetaExpand [increment=1.0]";
        assertEquals(expected, thetaExpand.toString());
    }

}
