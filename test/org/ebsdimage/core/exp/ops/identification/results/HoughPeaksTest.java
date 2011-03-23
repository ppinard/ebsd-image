package org.ebsdimage.core.exp.ops.identification.results;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;

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

        peak1 = new HoughPeak(0.5, 3.0, 1);
        peak2 = new HoughPeak(1.5, 5.0, 3);
        peak3 = new HoughPeak(1.0, 4.0, 2);

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
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        HoughPeaks other = new XmlLoader().load(HoughPeaks.class, file);
        assertEquals(op, other, 1e-6);
    }
}
