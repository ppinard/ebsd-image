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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.Camera;
import org.junit.Before;
import org.junit.Test;

public class CameraTest {

    private Camera camera;



    @Before
    public void setUp() throws Exception {
        camera = new Camera(0.3, 0.4, 0.5);
    }



    @Test
    public void testCameraDoubleDoubleDouble() {
        assertEquals(0.3, camera.patternCenterH, 1e-7);
        assertEquals(0.4, camera.patternCenterV, 1e-7);
        assertEquals(0.5, camera.detectorDistance, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testCameraDoubleDoubleDoubleException1() {
        new Camera(-1.0, 0.0, 0.3); // Throw exception
    }



    @Test(expected = IllegalArgumentException.class)
    public void testCameraDoubleDoubleDoubleException2() {
        new Camera(0.0, -1.0, 0.3); // Throw exception
    }



    @Test(expected = IllegalArgumentException.class)
    public void testCameraDoubleDoubleDoubleException3() {
        new Camera(0.0, 0.0, -1.0); // Throw exception
    }



    @Test
    public void testEqualsCamera() {
        Camera camera = new Camera(0.4, 0.4, 0.3);
        assertTrue(camera.equals(camera));
    }



    @Test
    public void testEqualsCameraDouble() {
        Camera camera = new Camera(0.401, 0.4, 0.3);
        assertTrue(camera.equals(camera, 1e-2));
    }



    @Test
    public void testToString() {
        assertEquals("PCh:0.3 PCv:0.4 DD:0.5", camera.toString());
    }

}
