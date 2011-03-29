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
import org.ebsdimage.core.AcquisitionConfig;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.vendors.hkl.core.HklMMapTester;
import org.ebsdimage.vendors.hkl.core.HklMetadata;
import org.junit.Test;

import crystallography.core.Crystal;
import crystallography.io.CrystalLoader;

import static org.junit.Assert.assertEquals;

import static ptpshared.geom.Assert.assertEquals;

public class CtfLoaderTest extends HklMMapTester {

    private File file;

    private HklMetadata metadata;



    @Override
    public void setUp() throws Exception {
        super.setUp();

        file = getFile("org/ebsdimage/vendors/hkl/testdata/Project19.ctf");

        Crystal copperPhase =
                new CrystalLoader().load(getFile("org/ebsdimage/vendors/hkl/testdata/Copper.xml"));

        CtfLoader loader = new CtfLoader();
        metadata = loader.loadMetadata(file, Microscope.DEFAULT);
        mmap = loader.load(file, metadata, new Crystal[] { copperPhase });
    }



    @Test
    public void testLoadMetadata() {
        assertEquals("Project19", metadata.projectName);

        AcquisitionConfig acqConfig = metadata.acquisitionConfig;
        assertEquals(Rotation.IDENTITY, acqConfig.sampleRotation, 1e-6);
        assertEquals(20e3, acqConfig.beamEnergy, 1e-6);
        assertEquals(70, Math.toDegrees(acqConfig.tiltAngle), 1e-6);
        assertEquals(600, acqConfig.magnification, 1e-6);
    }



    @Test
    public void testLoadPhaseNames() throws IOException {
        String[] phaseNames = new CtfLoader().loadPhaseNames(file);

        assertEquals(1, phaseNames.length);
        assertEquals("Copper", phaseNames[0]);
    }

}
