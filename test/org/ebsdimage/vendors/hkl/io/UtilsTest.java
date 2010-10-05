package org.ebsdimage.vendors.hkl.io;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

public class UtilsTest {

    @Before
    public void setUp() throws Exception {
    }



    @Test
    public void testGetPatternImagesDir() {
        File ctfFile =
                FileUtil.getFile("org/ebsdimage/vendors/hkl/testdata/Project19.ctf");
        File expected =
                FileUtil.getFile("org/ebsdimage/vendors/hkl/testdata/Project19Images");

        assertEquals(expected, Utils.getPatternImagesDir(ctfFile));
    }

}