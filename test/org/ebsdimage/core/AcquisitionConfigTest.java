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
package org.ebsdimage.core;

import java.io.File;
import java.io.IOException;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.geom.EuclideanSpace;
import ptpshared.geom.Plane;
import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class AcquisitionConfigTest extends TestCase {

    private AcquisitionConfig acqConfig;

    private Microscope microscope;



    @Before
    public void setUp() throws Exception {
        Camera camera =
                new Camera(new Vector3D(1, 0, 0), new Vector3D(0, -1, 0), 0.04,
                        0.03);
        Vector3D tiltAxis = new Vector3D(0, 1, 0);
        microscope = new Microscope("Name1", camera, tiltAxis);

        Rotation r = new Rotation(new Vector3D(0, 0, 1), Math.toRadians(90.0));

        acqConfig =
                new AcquisitionConfig(microscope, Math.toRadians(45), 0.015,
                        15e3, 1000, r, 0.5, 0.1, 0.02);
    }



    @Test
    public void testEqualsObjectObject() {
        assertTrue(acqConfig.equals(acqConfig, 1e-3));
        assertFalse(acqConfig.equals(null, 1e-3));
        assertFalse(acqConfig.equals(new Object(), 1e-3));
    }



    @Test
    public void testGetAcquisitionPositionCS() {
        double S2 = Math.sqrt(2) / 2;

        EuclideanSpace cs = acqConfig.getAcquisitionPositionCS();

        assertEquals(new Vector3D(0, 1, 0), cs.i, 1e-6);
        assertEquals(new Vector3D(-S2, 0, S2), cs.j, 1e-6);
        assertEquals(new Vector3D(S2, 0, S2), cs.k, 1e-6);
        assertEquals(new Vector3D(0, 0, -acqConfig.workingDistance),
                cs.translation, 1e-6);
    }



    @Test
    public void testAcquisitionConfiguration() {
        assertEquals(15e3, acqConfig.beamEnergy, 1e-6);
        assertEquals(microscope.camera, acqConfig.camera, 1e-6);
        assertEquals(0.02, acqConfig.cameraDistance, 1e-6);
        assertEquals(1000, acqConfig.magnification, 1e-6);
        assertEquals(0.5, acqConfig.patternCenterX, 1e-6);
        assertEquals(0.1, acqConfig.patternCenterY, 1e-6);

        Rotation expected =
                new Rotation(new Vector3D(0, 0, 1), Math.toRadians(90.0));
        assertEquals(expected, acqConfig.sampleRotation, 1e-6);
        assertEquals(Math.toRadians(45.0), acqConfig.tiltAngle, 1e-6);
        assertEquals(new Vector3D(0, 1, 0), acqConfig.tiltAxis, 1e-6);
        assertEquals(0.015, acqConfig.workingDistance, 1e-6);
    }



    @Test
    public void testGetCameraCS() {
        EuclideanSpace cs = acqConfig.getCameraCS();

        assertEquals(new Vector3D(0, -1, 0), cs.i, 1e-6);
        assertEquals(Vector3D.crossProduct(cs.k, cs.i), cs.j, 1e-6);
        assertEquals(new Vector3D(1, 0, 0), cs.k, 1e-6);
        assertEquals(new Vector3D(0.02, 0.02, -0.01), cs.translation, 1e-2);
    }



    @Test
    public void testGetCameraPlane() {
        Plane expected =
                new Plane(new Vector3D(0.02, 0.02, -0.01),
                        new Vector3D(1, 0, 0));
        assertEquals(expected, acqConfig.getCameraPlane(), 1e-2);
    }



    @Test
    public void testGetMicroscopeCS() {
        assertEquals(EuclideanSpace.ORIGIN, acqConfig.getMicroscopeCS(), 1e-6);
    }



    @Test
    public void testXML() throws IOException {
        File file = createTempFile();

        XmlSaver saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.save(acqConfig, file);

        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());
        AcquisitionConfig other = loader.load(AcquisitionConfig.class, file);

        assertEquals(new Vector3D(1, 0, 0), other.camera.n);
        assertEquals(new Vector3D(0, -1, 0), other.camera.x);
        assertEquals(0.04, other.camera.width, 1e-6);
        assertEquals(0.03, other.camera.height, 1e-6);

        assertEquals(new Vector3D(0, 1, 0), other.tiltAxis, 1e-6);

        assertEquals(0.02, other.cameraDistance, 1e-6);
        assertEquals(0.5, other.patternCenterX, 1e-6);
        assertEquals(0.1, other.patternCenterY, 1e-6);

        Rotation expected =
                new Rotation(new Vector3D(0, 0, 1), Math.toRadians(90.0));
        assertEquals(expected, other.sampleRotation, 1e-6);

        assertEquals(Math.toRadians(45.0), other.tiltAngle, 1e-6);
        assertEquals(0.015, other.workingDistance, 1e-6);
    }
}
