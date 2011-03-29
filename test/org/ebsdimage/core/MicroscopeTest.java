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

import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

public class MicroscopeTest extends TestCase {

    private Microscope microscope;

    private Camera camera;

    private Vector3D tiltAxis;



    @Before
    public void setUp() throws Exception {
        camera =
                new Camera(new Vector3D(1, 0, 0), new Vector3D(0, -1, 0), 0.04,
                        0.03);
        tiltAxis = new Vector3D(0, 1, 0);
        microscope = new Microscope("Name1", camera, tiltAxis);
    }



    @Test
    public void testEqualsObjectObject() {
        assertTrue(microscope.equals(microscope, 1e-3));
        assertFalse(microscope.equals(null, 1e-3));
        assertFalse(microscope.equals(new Object(), 1e-3));
    }



    @Test
    public void testMicroscopeDEFAULT() {
        Microscope microscope = Microscope.DEFAULT;

        assertEquals("Unnamed", microscope.name);

        assertEquals(Camera.NO_CAMERA, microscope.camera);

        assertEquals(new Vector3D(0, 1, 0), microscope.tiltAxis, 1e-6);
    }



    @Test
    public void testMicroscopeStringCameraVector3D() {
        assertEquals("Name1", microscope.name);

        assertEquals(new Vector3D(1, 0, 0), microscope.camera.n);
        assertEquals(new Vector3D(0, -1, 0), microscope.camera.x);
        assertEquals(0.04, microscope.camera.width, 1e-6);
        assertEquals(0.03, microscope.camera.height, 1e-6);

        assertEquals(new Vector3D(0, 1, 0), microscope.tiltAxis, 1e-6);
    }



    @Test
    public void testXML() throws IOException {
        File file = createTempFile();

        XmlSaver saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.save(microscope, file);

        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());
        Microscope other = loader.load(Microscope.class, file);

        assertEquals(new Vector3D(1, 0, 0), other.camera.n);
        assertEquals(new Vector3D(0, -1, 0), other.camera.x);
        assertEquals(0.04, other.camera.width, 1e-6);
        assertEquals(0.03, other.camera.height, 1e-6);

        assertEquals(new Vector3D(0, 1, 0), other.tiltAxis, 1e-6);
    }

}
