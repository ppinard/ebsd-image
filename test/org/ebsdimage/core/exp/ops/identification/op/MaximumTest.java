package org.ebsdimage.core.exp.ops.identification.op;

import java.io.File;

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

import static junittools.test.Assert.assertEquals;

public class MaximumTest extends TestCase {

    private Maximum op;

    private HoughMap houghMap;

    private BinMap peaksMap;



    @Before
    public void setUp() throws Exception {
        op = new Maximum();

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
    public void testIdentify() {
        HoughPeak[] destPeaks = op.identify(null, peaksMap, houghMap);

        assertEquals(3, destPeaks.length);

        // Peak 1
        assertEquals(50.80506134033203, destPeaks[0].rho, 1e-4);
        assertEquals(2.670353651046753, destPeaks[0].theta, 1e-4);
        assertEquals(153.0, destPeaks[0].intensity, 1e-4);

        // Peak 2
        assertEquals(39.515045166015625, destPeaks[1].rho, 1e-4);
        assertEquals(2.1991147994995117, destPeaks[1].theta, 1e-4);
        assertEquals(128.0, destPeaks[1].intensity, 1e-4);

        // Peak 3
        assertEquals(30.10670280456543, destPeaks[2].rho, 1e-4);
        assertEquals(0.5235987901687622, destPeaks[2].theta, 1e-4);
        assertEquals(121.0, destPeaks[2].intensity, 1e-4);
    }



    @Test
    public void testToString() {
        String expected = "Maximum";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Maximum other = new XmlLoader().load(Maximum.class, file);
        assertEquals(op, other, 1e-6);
    }
}
