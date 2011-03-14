package org.ebsdimage.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class EbsdMetadataTest {

    private EbsdMetadata metadata;



    @Before
    public void setUp() throws Exception {
        metadata = EbsdMetadata.DEFAULT;
    }



    @Test
    public void testEbsdMetadata() {
        assertEquals(new Microscope(), metadata.microscope, 1e-6);
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

}
