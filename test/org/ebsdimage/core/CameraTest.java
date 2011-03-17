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
package org.ebsdimage.core;

import java.io.File;
import java.io.IOException;

import magnitude.core.Magnitude;

import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.Calibration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class CameraTest extends TestCase {

    private Camera camera;



    @Before
    public void setUp() throws Exception {
        Vector3D n = new Vector3D(1, 0, 0);
        Vector3D x = new Vector3D(0, -1, 0);
        camera = new Camera(n, x, 0.04, 0.03);
    }



    @Test
    public void testCameraVector3DVector3DDoubleDouble() {
        assertEquals(new Vector3D(1, 0, 0), camera.n);
        assertEquals(new Vector3D(0, -1, 0), camera.x);
        assertEquals(0.04, camera.width, 1e-6);
        assertEquals(0.03, camera.height, 1e-6);
    }



    @Test
    public void testCameraVector3DVector3DMagnitudeMagnitude() {
        Vector3D n = new Vector3D(1, 0, 0);
        Vector3D x = new Vector3D(0, -1, 0);
        Magnitude width = new Magnitude(4, "cm");
        Magnitude height = new Magnitude(3, "cm");
        Camera camera = new Camera(n, x, width, height);

        assertEquals(n, camera.n);
        assertEquals(x, camera.x);
        assertEquals(0.04, camera.width, 1e-6);
        assertEquals(0.03, camera.height, 1e-6);
    }



    @Test
    public void testEqualsCameraObjectObject() {
        assertTrue(camera.equals(camera, 1e-3));
        assertFalse(camera.equals(null, 1e-3));
        assertFalse(camera.equals(new Object(), 1e-3));

        Camera other =
                new Camera(new Vector3D(1.1, 0.001, 0.001), new Vector3D(0.001,
                        -1.001, 0.001), 0.0401, 0.0301);
        assertFalse(camera.equals(other, 1e-3));

        other =
                new Camera(new Vector3D(1.001, 0.001, 0.001), new Vector3D(0.1,
                        -1.001, 0.001), 0.0401, 0.0301);
        assertFalse(camera.equals(other, 1e-3));

        other =
                new Camera(new Vector3D(1.001, 0.001, 0.001), new Vector3D(
                        0.001, -1.001, 0.001), 0.041, 0.0301);
        assertFalse(camera.equals(other, 1e-3));

        other =
                new Camera(new Vector3D(1.001, 0.001, 0.001), new Vector3D(
                        0.001, -1.001, 0.001), 0.0401, 0.031);
        assertFalse(camera.equals(other, 1e-3));

        other =
                new Camera(new Vector3D(1.001, 0.001, 0.001), new Vector3D(
                        0.001, -1.001, 0.001), 0.0401, 0.0301);
        assertTrue(camera.equals(other, 1e-3));
    }



    @Test
    public void testGetCalibration() {
        Calibration cal = camera.getCalibration(400, 300);
        assertEquals(0.0001, cal.dx, 1e-6);
        assertEquals(0.0001, cal.dy, 1e-6);

        cal = camera.getCalibration(200, 150);
        assertEquals(0.0002, cal.dx, 1e-6);
        assertEquals(0.0002, cal.dy, 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetCalibrationException1() {
        camera.getCalibration(-1, 300);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetCalibrationException2() {
        camera.getCalibration(400, -1);
    }



    @Test
    public void testGetWidth() {
        Magnitude expected = new Magnitude(4, "cm");
        assertEquals(expected, camera.getWidth(), 1e-6);
    }



    @Test
    public void testGetHeight() {
        Magnitude expected = new Magnitude(3, "cm");
        assertEquals(expected, camera.getHeight(), 1e-6);
    }



    @Test
    public void testXML() throws IOException {
        File file = createTempFile();

        XmlSaver saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.save(camera, file);

        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());
        Camera other = loader.load(Camera.class, file);

        assertEquals(new Vector3D(1, 0, 0), other.n);
        assertEquals(new Vector3D(0, -1, 0), other.x);
        assertEquals(0.04, other.width, 1e-6);
        assertEquals(0.03, other.height, 1e-6);
    }

}
