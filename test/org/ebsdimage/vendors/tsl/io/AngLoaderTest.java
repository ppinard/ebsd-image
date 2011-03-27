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
package org.ebsdimage.vendors.tsl.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.vendors.tsl.core.TslMMapTester;
import org.ebsdimage.vendors.tsl.core.TslMetadata;
import org.junit.Test;

import crystallography.core.Crystal;
import crystallography.io.CrystalLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

public class AngLoaderTest extends TslMMapTester {

    private File file;

    private AngLoader loader;



    public AngLoaderTest() throws Exception {
        file = getFile("org/ebsdimage/vendors/tsl/testdata/scan1.ang");
        loader = new AngLoader();

        Crystal nickel =
                new CrystalLoader().load(getFile("org/ebsdimage/vendors/tsl/testdata/Nickel.xml"));
        Crystal wc =
                new CrystalLoader().load(getFile("org/ebsdimage/vendors/tsl/testdata/WC.xml"));

        TslMetadata metadata = loader.loadMetadata(file, new Microscope());
        mmap = loader.load(file, metadata, new Crystal[] { wc, nickel });
    }



    @Test
    public void testCanLoad() {
        assertTrue(loader.canLoad(file));
        assertFalse(loader.canLoad(getFile("org/ebsdimage/vendors/tsl/testdata/WC.xml")));
    }



    @Test
    public void testLoadMetadata() throws IOException {
        Camera camera =
                new Camera(new Vector3D(1, 0, 0), new Vector3D(0, -1, 0), 0.04,
                        0.03);
        Microscope microscope = new Microscope(camera, new Vector3D(0, 1, 0));
        microscope.setBeamEnergy(15e3);

        TslMetadata metadata = loader.loadMetadata(file, microscope);
        Microscope other = metadata.getMicroscope();

        assertEquals(0.523500, other.getPatternCenterX(), 1e-6);
        assertEquals(1 - 0.804900, other.getPatternCenterY(), 1e-6);
        assertEquals(0.770600 * 0.04, other.getCameraDistance(), 1e-6);
        assertEquals(15, other.getWorkingDistance() * 1000, 1e-3);
        assertEquals(Rotation.IDENTITY, other.getSampleRotation(), 1e-6);
        assertEquals(15e3, other.getBeamEnergy(), 1e-6);
    }



    @Test
    public void testLoadPhaseNames() throws IOException {
        String[] phaseNames = loader.loadPhaseNames(file);

        assertEquals(2, phaseNames.length);
        assertEquals("TungstenCarbide", phaseNames[0]);
        assertEquals("Nickel", phaseNames[1]);
    }

}
