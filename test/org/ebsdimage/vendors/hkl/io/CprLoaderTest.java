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
package org.ebsdimage.vendors.hkl.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.AcquisitionConfig;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.vendors.hkl.core.HklMetadata;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

public class CprLoaderTest extends TestCase {

    private CprLoader loader;

    private File file;



    @Before
    public void setUp() throws Exception {
        file = getFile("org/ebsdimage/vendors/hkl/testdata/Project1.cpr");
        loader = new CprLoader();
    }



    @Test
    public void testCanLoad() {
        assertTrue(loader.canLoad(file));
        assertFalse(loader.canLoad(getFile("org/ebsdimage/vendors/hkl/testdata/Copper.xml")));
    }



    @Test
    public void testLoad() throws IOException {
        HklMetadata metadata = loader.load(file, Microscope.DEFAULT);

        assertEquals("Project1", metadata.projectName);

        AcquisitionConfig acqConfig = metadata.acquisitionConfig;
        assertEquals(250, acqConfig.magnification, 1e-6);
        assertEquals(15e3, acqConfig.beamEnergy, 1e-6);
        assertEquals(Math.toRadians(70.0), acqConfig.tiltAngle, 1e-6);
        assertEquals(0.52451, acqConfig.patternCenterX, 1e-6);
        assertEquals(0.58419, acqConfig.patternCenterY, 1e-6);
        assertEquals(0.0, acqConfig.cameraDistance, 1e-6);
        assertEquals(new Rotation(RotationOrder.ZXZ, 0.0, 0.0, 0.0),
                acqConfig.sampleRotation, 1e-6);
    }

}
