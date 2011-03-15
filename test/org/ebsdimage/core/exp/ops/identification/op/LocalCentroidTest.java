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
    public void testIdentify() {
        HoughPeak[] destPeaks = op.identify(null, peaksMap, houghMap);

        assertEquals(3, destPeaks.length);

        // Peak 1
        assertEquals(48.8243560791056, destPeaks[0].rho, 1e-4);
        assertEquals(2.675865411758423, destPeaks[0].theta, 1e-4);
        assertEquals(142.0, destPeaks[0].intensity, 1e-4);

        // Peak 2
        assertEquals(37.74406433105469, destPeaks[1].rho, 1e-4);
        assertEquals(2.202194929122925, destPeaks[1].theta, 1e-4);
        assertEquals(128.0, destPeaks[1].intensity, 1e-4);

        // Peak 3
        assertEquals(29.354036331176758, destPeaks[2].rho, 1e-4);
        assertEquals(0.5340707302093506, destPeaks[2].theta, 1e-4);
        assertEquals(121.0, destPeaks[2].intensity, 1e-4);
    }



    @Test
    public void testToString() {
        String expected = "Local Centroid";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        LocalCentroid other = new XmlLoader().load(LocalCentroid.class, file);
        assertEquals(op, other, 1e-6);
    }
}
