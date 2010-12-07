package org.ebsdimage.core.exp.ops.identification.results;

import java.io.File;
import java.io.IOException;

import magnitude.core.Magnitude;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class HoughPeaksTest extends TestCase {

    private HoughPeaks op;

    private HoughPeak peak1;

    private HoughPeak peak2;

    private HoughPeak peak3;

    private HoughPeak[] peaks;



    @Before
    public void setUp() throws Exception {
        op = new HoughPeaks();

        Magnitude theta = new Magnitude(0.5, "rad");
        Magnitude rho = new Magnitude(3.0, "px");
        peak1 = new HoughPeak(theta, rho, 1);

        theta = new Magnitude(1.5, "rad");
        rho = new Magnitude(5.0, "px");
        peak2 = new HoughPeak(theta, rho, 3);

        theta = new Magnitude(1.0, "rad");
        rho = new Magnitude(4.0, "px");
        peak3 = new HoughPeak(theta, rho, 2);

        peaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testSave() throws IOException {
        File file = createTempFile();
        op.save(peaks, file);

        HoughPeak[] other = new XmlLoader().loadArray(HoughPeak.class, file);

        assertEquals(3, other.length);
        assertEquals(peak2, other[0], 1e-6);
        assertEquals(peak3, other[1], 1e-6);
        assertEquals(peak1, other[2], 1e-6);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new HoughPeaks()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new HoughPeaks(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(1995310916, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        HoughPeaks other = new XmlLoader().load(HoughPeaks.class, file);
        assertEquals(op, other, 1e-6);
    }
}
