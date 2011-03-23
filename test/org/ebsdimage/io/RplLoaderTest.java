package org.ebsdimage.io;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RplLoaderTest extends TestCase {

    private RplLoader loader;

    private File file;



    @Before
    public void setUp() throws Exception {
        loader = new RplLoader();
        file = getFile("org/ebsdimage/testdata/warp-x-map.rpl");
    }



    @Test
    public void testCanLoad() {
        assertTrue(loader.canLoad(file));
        assertFalse(loader.canLoad(getFile("org/ebsdimage/testdata/warp-x-map.raw")));
    }



    @Test
    public void testLoadFile() throws IOException {
        RplFile rpl = loader.load(file);
        testRplFile(rpl);
    }



    @Test
    public void testLoadFileObject() throws IOException {
        RplFile rpl = loader.load(file, null);
        testRplFile(rpl);
    }



    private void testRplFile(RplFile rpl) {
        assertEquals(1, rpl.dataLength);
        assertEquals(168, rpl.width);
        assertEquals(128, rpl.height);
        assertTrue(rpl.isBigEndian());
        assertFalse(rpl.isSigned());
    }
}
