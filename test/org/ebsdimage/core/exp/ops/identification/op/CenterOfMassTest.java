package org.ebsdimage.core.exp.ops.identification.op;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Threshold;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;
import rmlimage.core.MapMath;
import rmlshared.io.FileUtil;

public class CenterOfMassTest {

    private CenterOfMass centerOfMass;

    private HoughMap houghMap;

    private BinMap peaksMap;



    @Before
    public void setUp() throws Exception {
        centerOfMass = new CenterOfMass();

        houghMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap_cropped.bmp"));

        // Create peaks map with three peaks;
        IdentMap identMap =
                Identification.identify(Threshold.automaticTopHat(houghMap));
        peaksMap = new BinMap(identMap.width, identMap.height);

        MapMath.or(peaksMap, Identification.extractObject(identMap, 1),
                peaksMap);
        MapMath.or(peaksMap, Identification.extractObject(identMap, 2),
                peaksMap);
        MapMath.or(peaksMap, Identification.extractObject(identMap, 3),
                peaksMap);
    }



    @Test
    public void testIdentify() throws Exception {
        HoughPeak[] destPeaks = centerOfMass.identify(null, peaksMap, houghMap);

        assertEquals(3, destPeaks.length);

        // Peak 1
        assertEquals(88.09204877, destPeaks[0].rho, 1e-6);
        assertEquals(2.525296303, destPeaks[0].theta, 1e-6);
        assertEquals(123.0, destPeaks[0].intensity, 1e-6);

        // Peak 2
        assertEquals(87.4705610, destPeaks[1].rho, 1e-6);
        assertEquals(2.927335805, destPeaks[1].theta, 1e-6);
        assertEquals(125.0, destPeaks[1].intensity, 1e-6);

        // Peak 3
        assertEquals(87.46666666, destPeaks[2].rho, 1e-6);
        assertEquals(1.274090353, destPeaks[2].theta, 1e-6);
        assertEquals(123.0, destPeaks[2].intensity, 1e-6);
    }



    @Test
    public void testToString() {
        String expected = "Center of Mass";
        assertEquals(expected, centerOfMass.toString());

    }

}
