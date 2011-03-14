package org.ebsdimage.core.exp.ops.identification.op;

import java.io.File;
import java.util.Arrays;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Threshold;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;
import rmlimage.core.MapMath;
import rmlshared.io.FileUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class LocalCentroidTest extends TestCase {

    private LocalCentroid op;

    private HoughMap houghMap;

    private BinMap peaksMap;



    @Before
    public void setUp() throws Exception {
        op = new LocalCentroid();

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
    public void testToString() {
        String expected = "Local Centroid";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testIdentify() {
        HoughPeak[] destPeaks = op.identify(null, peaksMap, houghMap);

        assertEquals(3, destPeaks.length);

        System.out.println(Arrays.toString(destPeaks));

        // Peak 1
        assertEquals(48.92339324951172, destPeaks[0].rho.getValue("px"), 1e-6);
        assertEquals(2.670353651046753, destPeaks[0].theta.getValue("rad"),
                1e-6);
        assertEquals(142.0, destPeaks[0].intensity, 1e-6);

        // Peak 2
        assertEquals(37.63337707519531, destPeaks[1].rho.getValue("px"), 1e-6);
        assertEquals(2.1991147994995117, destPeaks[1].theta.getValue("rad"),
                1e-6);
        assertEquals(125.0, destPeaks[1].intensity, 1e-6);

        // Peak 3
        assertEquals(30.10670280456543, destPeaks[2].rho.getValue("px"), 1e-6);
        assertEquals(0.5410520434379578, destPeaks[2].theta.getValue("rad"),
                1e-6);
        assertEquals(121.0, destPeaks[2].intensity, 1e-6);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new LocalCentroid()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new LocalCentroid(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-1490956726, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        LocalCentroid other = new XmlLoader().load(LocalCentroid.class, file);
        assertEquals(op, other, 1e-6);
    }
}
