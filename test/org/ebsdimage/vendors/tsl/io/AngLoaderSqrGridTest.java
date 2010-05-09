/*
 * EBSD-Image
 * Copyright (C) 2010 Philippe T. Pinard
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.vendors.tsl.core.TslMMapTester;
import org.ebsdimage.vendors.tsl.core.TslMetadata;
import org.ebsdimage.vendors.tsl.io.AngLoader;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;
import crystallography.io.CrystalLoader;

public class AngLoaderSqrGridTest extends TslMMapTester {

    private File file;



    public AngLoaderSqrGridTest() throws Exception {
        file = getFile("org/ebsdimage/vendors/tsl/testdata/scan1.ang");

        Crystal nickel =
                new CrystalLoader()
                        .load(getFile("org/ebsdimage/vendors/tsl/testdata/Nickel.xml"));
        Crystal wc =
                new CrystalLoader()
                        .load(getFile("org/ebsdimage/vendors/tsl/testdata/WC.xml"));

        mmap =
                new AngLoader().load(file, EbsdMetadata.DEFAULT_BEAM_ENERGY,
                        EbsdMetadata.DEFAULT_MAGNIFICATION,
                        EbsdMetadata.DEFAULT_TILT_ANGLE,
                        EbsdMetadata.DEFAULT_SAMPLE_ROTATION, new Crystal[] {
                                wc, nickel });
    }



    @Test
    public void testGetMetadataStatic() throws IOException {
        TslMetadata metadata =
                AngLoader.getMetadata(file, EbsdMetadata.DEFAULT_BEAM_ENERGY,
                        EbsdMetadata.DEFAULT_MAGNIFICATION,
                        EbsdMetadata.DEFAULT_TILT_ANGLE,
                        EbsdMetadata.DEFAULT_SAMPLE_ROTATION);

        assertEquals(Double.NaN, metadata.beamEnergy, 1e-3);
        assertEquals(Double.NaN, metadata.magnification, 1e-3);
        assertEquals(Double.NaN, metadata.tiltAngle, 1e-3);
        assertEquals(15, metadata.workingDistance * 1000, 1e-3);
        assertEquals(0.07, metadata.pixelWidth * 1e6, 1e-3);
        assertEquals(0.07, metadata.pixelHeight * 1e6, 1e-3);
        assertTrue(Quaternion.IDENTITY.equals(metadata.sampleRotation, 1e-3));
        assertTrue(new Camera(0.0235, 0.3049, 0.7706).equals(
                metadata.calibration, 1e-3));
    }



    @Test
    public void testGetPhaseNames() throws IOException {
        String[] phaseNames = AngLoader.getPhaseNames(file);

        assertEquals(2, phaseNames.length);
        assertEquals("TungstenCarbide", phaseNames[0]);
        assertEquals("Nickel", phaseNames[1]);
    }



    @Test
    public void testIsAng() {
        assertTrue(AngLoader.isAng(file));
        assertFalse(AngLoader
                .isAng(getFile("org/ebsdimage/vendors/tsl/testdata/WC.xml")));
    }

}
