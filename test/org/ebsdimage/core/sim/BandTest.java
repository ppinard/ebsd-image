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
package org.ebsdimage.core.sim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.sim.Band;
import org.ebsdimage.core.sim.BandException;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.geom.Line;
import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;
import crystallography.core.Plane;
import crystallography.core.Reflector;
import crystallography.core.XrayScatteringFactors;
import crystallography.core.crystals.Silicon;

public class BandTest {

    private Plane plane;
    private Crystal crystal;
    private XrayScatteringFactors scatter;
    private Reflector refl;
    private Quaternion rotation;
    private Camera camera;
    private double energy;
    private Band band;



    @Before
    public void setUp() throws Exception {
        plane = new Plane(1, 1, 1);
        crystal = new Silicon();
        scatter = new XrayScatteringFactors();

        refl = new Reflector(plane, crystal, scatter);
        rotation = Quaternion.IDENTITY;
        camera = new Camera(0.0, 0.0, 0.3);
        energy = 20e3;

        band = new Band(refl, rotation, camera, energy);
    }



    @Test
    public void testBand() {
        assertEquals(plane, band.plane);
        assertEquals(refl.planeSpacing, band.planeSpacing, 1e-7);
        assertEquals(refl.intensity, band.intensity, 1e-7);
        assertEquals(refl.normalizedIntensity, band.normalizedIntensity, 1e-7);
        assertEquals(new Line(-1.0, -0.3), band.line);
        assertEquals(0.005938423, band.halfWidths[0], 1e-7);
        assertEquals(0.006051357, band.halfWidths[1], 1e-7);
        assertEquals(-0.29160180, band.edgeIntercepts[0].k, 1e-7);
        assertEquals(-0.30855791, band.edgeIntercepts[1].k, 1e-7);
        assertEquals(0.006051357 * 2, band.width, 1e-7);
    }



    @Test(expected = BandException.class)
    public void testCalculateQuaternionCameraDoubleException() {
        Plane plane = new Plane(0, 1, 0);
        Reflector refl = new Reflector(plane, crystal, scatter);
        new Band(refl, rotation, camera, energy); // Throw exception
    }



    @Test
    public void testEqualsBandDouble() {
    }



    @Test
    public void testEqualsObject() {
        assertTrue(band.equals(band));

        assertFalse(band.equals(new Object()));

        Reflector other = new Reflector(new Plane(3, 1, 1), crystal, scatter);
        assertFalse(band.equals(new Band(other, rotation, camera, energy)));
    }



    @Test
    public void testToString() {
        String expected = "Band (1;1;1)\n";
        assertTrue(band.toString().startsWith(expected));
    }

}
