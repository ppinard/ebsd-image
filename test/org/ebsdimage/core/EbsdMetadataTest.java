package org.ebsdimage.core;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class EbsdMetadataTest extends TestCase {

    private EbsdMetadata metadata;



    @Before
    public void setUp() throws Exception {
        metadata = EbsdMetadata.DEFAULT;
    }



    @Test
    public void testEbsdMetadata() {
        assertEquals(new Microscope(), metadata.getMicroscope(), 1e-6);
    }



    @Test
    public void testEqualsObjectObject() {
        assertTrue(metadata.equals(metadata, 1e-3));
        assertFalse(metadata.equals(null, 1e-3));
        assertFalse(metadata.equals(new Object(), 1e-3));

        Microscope microscope = new Microscope();
        microscope.setCameraDistance(0.01);
        EbsdMetadata other = new EbsdMetadata(microscope);
        assertFalse(metadata.equals(other, 1e-3));
        assertTrue(metadata.equals(other, 1e-2));
    }



    @Test
    public void testXML() throws IOException {
        File file = createTempFile();

        XmlSaver saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.save(metadata, file);

        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());
        EbsdMetadata other = loader.load(EbsdMetadata.class, file);

        assertEquals(other, metadata, 1e-6);
    }

}
