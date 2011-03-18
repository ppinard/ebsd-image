package org.ebsdimage.core.sim;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.Microscope;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import crystallography.core.ScatteringFactorsEnum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class SimMetadataTest extends TestCase {

    private SimMetadata metadata;



    @Before
    public void setUp() throws Exception {
        metadata = SimMetadata.DEFAULT;
    }



    @Test
    public void testEqualsObjectObject() {
        assertTrue(metadata.equals(metadata, 1e-3));
        assertFalse(metadata.equals(null, 1e-3));
        assertFalse(metadata.equals(new Object(), 1e-3));

        Microscope microscope = new Microscope();
        microscope.setCameraDistance(0.01);
        SimMetadata other =
                new SimMetadata(microscope, ScatteringFactorsEnum.XRAY, 4);
        assertFalse(metadata.equals(other, 1e-3));
        assertTrue(metadata.equals(other, 1e-2));
    }



    @Test
    public void testSimMetadata() {
        assertEquals(new Microscope(), metadata.microscope, 1e-6);
        assertEquals(ScatteringFactorsEnum.XRAY, metadata.scatteringFactors);
        assertEquals(4, metadata.maxIndex);
    }



    @Test
    public void testXML() throws IOException {
        File file = createTempFile();

        XmlSaver saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.save(metadata, file);

        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());
        SimMetadata other = loader.load(SimMetadata.class, file);

        assertEquals(other, metadata, 1e-6);
    }
}
