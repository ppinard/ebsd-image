/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ebsdimage.core.sim;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.AcquisitionConfig;
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
    }



    @Test
    public void testSimMetadata() {
        assertEquals(AcquisitionConfig.DEFAULT, metadata.acquisitionConfig,
                1e-6);
        assertEquals(ScatteringFactorsEnum.XRAY,
                metadata.getScatteringFactors());
        assertEquals(4, metadata.getMaxIndex());
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
