package org.ebsdimage.core.exp.ops.identification.results;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.ops.detection.op.AutomaticTopHat;
import org.ebsdimage.core.exp.ops.identification.op.CenterOfMass;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class PeaksHoughMapTest extends TestCase {

    private PeaksHoughMap op;



    @Before
    public void setUp() throws Exception {
        op = new PeaksHoughMap(5);
    }



    @Test
    public void testPeaksHoughMap() {
        assertEquals(5, op.count);
    }



    @Test
    public void testCreate() throws IOException {
        // Create
        HoughMap houghMap =
                new HoughMapLoader().load(getFile("org/ebsdimage/testdata/houghmap.bmp"));
        HoughPeak[] peaks =
                new CenterOfMass().identify(null,
                        new AutomaticTopHat().detect(null, houghMap), houghMap);
        File file = new File("/tmp/houghmap.bmp");

        houghMap.clear();
        op.create(houghMap, peaks, file);

        // Test
        BinMap peaksMap =
                Threshold.densitySlice((ByteMap) load(file), 255, 255);
        Centroid centroid =
                Analysis.getCentroid(Identification.identify(peaksMap));

        assertEquals(5, centroid.x.length);

        assertEquals(19, centroid.x[0], 1e-6);
        assertEquals(70, centroid.y[0], 1e-6);

        assertEquals(153, centroid.x[1], 1e-6);
        assertEquals(86, centroid.y[1], 1e-6);

        assertEquals(102, centroid.x[2], 1e-6);
        assertEquals(104, centroid.y[2], 1e-6);

        assertEquals(43, centroid.x[3], 1e-6);
        assertEquals(132, centroid.y[3], 1e-6);

        assertEquals(114, centroid.x[4], 1e-6);
        assertEquals(148, centroid.y[4], 1e-6);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Peaks HoughMap [count=5]");
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new PeaksHoughMap(4)));
        assertTrue(op.equals(new PeaksHoughMap(5)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 2));
        assertFalse(op.equals(null, 2));
        assertFalse(op.equals(new Object(), -2));

        assertFalse(op.equals(new PeaksHoughMap(3), 2));
        assertTrue(op.equals(new PeaksHoughMap(4), 2));
    }



    @Test
    public void testHashCode() {
        assertEquals(849393477, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        PeaksHoughMap other = new XmlLoader().load(PeaksHoughMap.class, file);
        assertEquals(op, other, 1e-6);
    }

}
