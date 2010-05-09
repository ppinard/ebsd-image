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
package org.ebsdimage.core.exp;

import static java.lang.Math.PI;
import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.exp.ExpMetadata;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;

public class ExpMetadataTest {

    private ExpMetadata metadata;
    private double beamEnergy;
    private double magnification;
    private double tiltAngle;
    private double workingDistance;
    private double pixelWidth;
    private double pixelHeight;
    private Quaternion sampleRotation;
    private Camera calibration;



    @Before
    public void setUp() throws Exception {
        beamEnergy = 20e3;
        magnification = 100;
        tiltAngle = toRadians(70);
        workingDistance = 15e-3;
        pixelWidth = 1e-6;
        pixelHeight = 2e-6;
        sampleRotation = new Quaternion(1, 2, 3, 4);
        calibration = new Camera(0.1, 0.2, 0.3);

        metadata =
                new ExpMetadata(beamEnergy, magnification, tiltAngle,
                        workingDistance, pixelWidth, pixelHeight,
                        sampleRotation, calibration);
    }



    @Test
    public void testExpMetadataDoubleDoubleDoubleDoubleDoubleDoubleQuaternionCamera() {
        assertEquals(beamEnergy, metadata.beamEnergy, 1e-6);
        assertEquals(magnification, metadata.magnification, 1e-6);
        assertEquals(tiltAngle, metadata.tiltAngle, 1e-6);
        assertEquals(workingDistance, metadata.workingDistance, 1e-6);
        assertEquals(pixelWidth, metadata.pixelWidth, 1e-6);
        assertEquals(pixelHeight, metadata.pixelHeight, 1e-6);
        assertTrue(sampleRotation.normalize().equals(metadata.sampleRotation,
                1e-6));
        assertTrue(calibration.equals(metadata.calibration, 1e-6));
    }



    @Test
    public void testExpMetadata() {
        ExpMetadata other = new ExpMetadata();

        assertEquals(ExpMetadata.DEFAULT_BEAM_ENERGY, other.beamEnergy, 1e-6);
        assertEquals(ExpMetadata.DEFAULT_MAGNIFICATION, other.magnification,
                1e-6);
        assertEquals(ExpMetadata.DEFAULT_TILT_ANGLE, other.tiltAngle, 1e-6);
        assertEquals(ExpMetadata.DEFAULT_WORKING_DISTANCE,
                other.workingDistance, 1e-6);
        assertEquals(ExpMetadata.DEFAULT_PIXEL_WIDTH, other.pixelWidth, 1e-6);
        assertEquals(ExpMetadata.DEFAULT_PIXEL_HEIGHT, other.pixelHeight, 1e-6);
        assertTrue(ExpMetadata.DEFAULT_SAMPLE_ROTATION.equals(
                other.sampleRotation, 1e-6));
        assertTrue(ExpMetadata.DEFAULT_CALIBRATION.equals(other.calibration,
                1e-6));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataBeamEnergyException1() {
        new ExpMetadata(Double.POSITIVE_INFINITY, magnification, tiltAngle,
                workingDistance, pixelWidth, pixelHeight, sampleRotation,
                calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataBeamEnergyException2() {
        new ExpMetadata(-1, magnification, tiltAngle, workingDistance,
                pixelWidth, pixelHeight, sampleRotation, calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataMagnificationException1() {
        new ExpMetadata(beamEnergy, Double.POSITIVE_INFINITY, tiltAngle,
                workingDistance, pixelWidth, pixelHeight, sampleRotation,
                calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataMagnificationException2() {
        new ExpMetadata(beamEnergy, -1, tiltAngle, workingDistance, pixelWidth,
                pixelHeight, sampleRotation, calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataTiltAngleException1() {
        new ExpMetadata(beamEnergy, magnification, Double.POSITIVE_INFINITY,
                workingDistance, pixelWidth, pixelHeight, sampleRotation,
                calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataTiltAngleException2() {
        new ExpMetadata(beamEnergy, magnification, -1, workingDistance,
                pixelWidth, pixelHeight, sampleRotation, calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataTiltAngleException3() {
        new ExpMetadata(beamEnergy, magnification, PI + 0.1, workingDistance,
                pixelWidth, pixelHeight, sampleRotation, calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataWorkingDistanceException1() {
        new ExpMetadata(beamEnergy, magnification, tiltAngle,
                Double.POSITIVE_INFINITY, pixelWidth, pixelHeight,
                sampleRotation, calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataWorkingDistanceException2() {
        new ExpMetadata(beamEnergy, magnification, tiltAngle, -1, pixelWidth,
                pixelHeight, sampleRotation, calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataPixelWidthException1() {
        new ExpMetadata(beamEnergy, magnification, tiltAngle, workingDistance,
                Double.POSITIVE_INFINITY, pixelHeight, sampleRotation,
                calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataPixelWidthException2() {
        new ExpMetadata(beamEnergy, magnification, tiltAngle, workingDistance,
                -1, pixelHeight, sampleRotation, calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataPixelHeightException1() {
        new ExpMetadata(beamEnergy, magnification, tiltAngle, workingDistance,
                pixelWidth, Double.POSITIVE_INFINITY, sampleRotation,
                calibration);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMetadataPixelHeightException2() {
        new ExpMetadata(beamEnergy, magnification, tiltAngle, workingDistance,
                pixelWidth, -1, sampleRotation, calibration);
    }



    @Test(expected = NullPointerException.class)
    public void testExpMetadataSampleRotationException() {
        new ExpMetadata(beamEnergy, magnification, tiltAngle, workingDistance,
                pixelWidth, pixelHeight, null, calibration);
    }



    @Test(expected = NullPointerException.class)
    public void testExpMetadataCalibrationException() {
        new ExpMetadata(beamEnergy, magnification, tiltAngle, workingDistance,
                pixelWidth, pixelHeight, sampleRotation, null);
    }

}
