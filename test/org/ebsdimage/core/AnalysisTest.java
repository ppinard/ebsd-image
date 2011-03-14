package org.ebsdimage.core;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;

import static org.junit.Assert.assertEquals;

public class AnalysisTest {

    private HoughMap houghMap;

    private IdentMap peaksMap;



    @Before
    public void setUp() throws Exception {
        houghMap = new HoughMap(5, 5, Math.toRadians(1), 2);

        Arrays.fill(houghMap.pixArray, (byte) 1);
        houghMap.pixArray[12] = (byte) 3;
        houghMap.pixArray[16] = (byte) 10;
        houghMap.pixArray[17] = (byte) 5;
        houghMap.pixArray[18] = (byte) 10;
        houghMap.pixArray[22] = (byte) 3;

        BinMap binMap = new BinMap(5, 5);
        binMap.setCalibration(houghMap);

        binMap.clear();
        binMap.pixArray[12] = (byte) 1;
        binMap.pixArray[16] = (byte) 1;
        binMap.pixArray[17] = (byte) 1;
        binMap.pixArray[18] = (byte) 1;
        binMap.pixArray[22] = (byte) 1;

        peaksMap = Identification.identify(binMap);
    }



    @Test
    public void testGetCentroid() {
        Centroid results = Analysis.getCentroid(peaksMap, houghMap);

        assertEquals(1, results.getValueCount());
        assertEquals(2.0, Math.toDegrees(results.x[0]), 1e-6);
        assertEquals(-2.0, results.y[0], 1e-6);
        assertEquals(5, results.intensity[0], 1e-6);
    }



    @Test
    public void testGetCentroidNoPeaks() {
        IdentMap peaksMap = new IdentMap(5, 5);
        peaksMap.setCalibration(houghMap);
        Centroid results = Analysis.getCentroid(peaksMap, houghMap);

        assertEquals(0, results.getValueCount());
    }



    @Test
    public void testGetCenterOfMass() {
        Centroid results = Analysis.getCenterOfMass(peaksMap, houghMap);

        assertEquals(1, results.getValueCount());
        assertEquals(2.0, Math.toDegrees(results.x[0]), 1e-6);
        assertEquals(-2.0, results.y[0], 1e-6);
        assertEquals(5, results.intensity[0], 1e-6);
    }



    @Test
    public void testGetCenterOfMassNoPeaks() {
        IdentMap peaksMap = new IdentMap(5, 5);
        peaksMap.setCalibration(houghMap);
        Centroid results = Analysis.getCenterOfMass(peaksMap, houghMap);

        assertEquals(0, results.getValueCount());
    }



    @Test
    public void testGetMaximumLocation() {
        Centroid results = Analysis.getMaximumLocation(peaksMap, houghMap);

        assertEquals(1, results.getValueCount());
        assertEquals(1.0, Math.toDegrees(results.x[0]), 1e-6);
        assertEquals(-2.0, results.y[0], 1e-6);
        assertEquals(10, results.intensity[0], 1e-6);
    }



    @Test
    public void testGetMaximumLocationNoPeaks() {
        IdentMap peaksMap = new IdentMap(5, 5);
        peaksMap.setCalibration(houghMap);
        Centroid results = Analysis.getMaximumLocation(peaksMap, houghMap);

        assertEquals(0, results.getValueCount());
    }



    @Test(expected = IllegalArgumentException.class)
    public void testValidateException1() {
        // different size
        IdentMap peaksMap = new IdentMap(6, 6);
        peaksMap.setCalibration(houghMap);

        Analysis.getCentroid(peaksMap, houghMap);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testValidateException2() {
        // different calibration
        IdentMap peaksMap = new IdentMap(5, 5);

        Analysis.getCentroid(peaksMap, houghMap);
    }

}
