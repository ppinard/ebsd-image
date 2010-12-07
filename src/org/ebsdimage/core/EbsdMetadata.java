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

import static java.lang.Math.PI;
import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import ptpshared.core.math.Quaternion;

/**
 * Representation of the metadata held in the <code>EbsdMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
@Root(name = "metadata")
@Immutable
public class EbsdMetadata implements AlmostEquable {

    /** Default EBSD metadata. */
    public static final EbsdMetadata DEFAULT = new EbsdMetadata(Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, new Camera(0.0, 0.0, 1.0),
            Quaternion.IDENTITY, Quaternion.IDENTITY);

    /** Energy of the electron beam in eV. */
    @Element(name = "beamEnergy")
    public final double beamEnergy;

    /** Calibration of the camera. */
    @Element(name = "camera")
    public final Camera camera;

    /** Rotation of the camera with respect to the microscope reference frame. */
    @Element(name = "cameraRotation")
    public final Quaternion cameraRotation;

    /** Magnification of the EBSD acquisition. */
    @Element(name = "magnification")
    public final double magnification;

    /** Rotation of the sample with respect to the microscope reference frame. */
    @Element(name = "sampleRotation")
    public final Quaternion sampleRotation;

    /** Angle of sample's tilt in radians. */
    @Element(name = "tiltAngle")
    public final double tiltAngle;

    /** Working distance of the EBSD acquisition in meters. */
    @Element(name = "workingDistance")
    public final double workingDistance;



    /**
     * Creates a new <code>EbsdMetadata</code> to store all the required
     * parameters of a <code>EbsdMMap</code>. All values are validated.
     * 
     * @param beamEnergy
     *            energy of the electron beam in eV
     * @param magnification
     *            magnification of the EBSD acquisition
     * @param tiltAngle
     *            angle of sample's tilt in radians
     * @param workingDistance
     *            working distance of the EBSD acquisition in meters
     * @param camera
     *            calibration of the camera
     * @param sampleRotation
     *            rotation of the sample with respect to the microscope
     *            reference frame
     * @param cameraRotation
     *            rotation of the camera with respect to the microscope
     *            reference frame
     */
    public EbsdMetadata(@Element(name = "beamEnergy") double beamEnergy,
            @Element(name = "magnification") double magnification,
            @Element(name = "tiltAngle") double tiltAngle,
            @Element(name = "workingDistance") double workingDistance,
            @Element(name = "camera") Camera camera,
            @Element(name = "sampleRotation") Quaternion sampleRotation,
            @Element(name = "cameraRotation") Quaternion cameraRotation) {
        // Beam energy
        if (Double.isInfinite(beamEnergy))
            throw new IllegalArgumentException("Beam energy is infinite");
        if (beamEnergy <= 0)
            throw new IllegalArgumentException("Invalid beamEnergy ("
                    + beamEnergy + ')');
        this.beamEnergy = beamEnergy;

        // Magnification
        if (Double.isInfinite(magnification))
            throw new IllegalArgumentException("magnification is infinite");
        if (magnification <= 0)
            throw new IllegalArgumentException("Invalid magnification ("
                    + magnification + ')');
        this.magnification = magnification;

        // Tilt angle
        if (Double.isInfinite(tiltAngle))
            throw new IllegalArgumentException("Tilt angle is infinite");
        if (tiltAngle < 0 || tiltAngle > PI / 2)
            throw new IllegalArgumentException("Invalid tilt angle ("
                    + tiltAngle + "). It must be between [0, PI/2].");
        this.tiltAngle = tiltAngle;

        // Working distance
        if (Double.isInfinite(workingDistance))
            throw new IllegalArgumentException("Working distance is infinite");
        if (workingDistance < 0)
            throw new IllegalArgumentException("Working distance ("
                    + workingDistance + ") must be greater than 0.");
        this.workingDistance = workingDistance;

        // Calibration
        if (camera == null)
            throw new NullPointerException("Calibration cannot be null.");
        this.camera = camera;

        // Sample rotation
        if (sampleRotation == null)
            throw new NullPointerException(
                    "The sample rotation cannot be null.");
        this.sampleRotation = sampleRotation.normalize();

        // camera rotation
        if (cameraRotation == null)
            throw new NullPointerException(
                    "The camera rotation cannot be null.");
        this.cameraRotation = cameraRotation.normalize();
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        EbsdMetadata other = (EbsdMetadata) obj;
        if (Double.doubleToLongBits(beamEnergy) != Double.doubleToLongBits(other.beamEnergy))
            return false;
        if (Double.doubleToLongBits(tiltAngle) != Double.doubleToLongBits(other.tiltAngle))
            return false;
        if (Double.doubleToLongBits(workingDistance) != Double.doubleToLongBits(other.workingDistance))
            return false;
        if (Double.doubleToLongBits(magnification) != Double.doubleToLongBits(other.magnification))
            return false;

        if (!camera.equals(other.camera))
            return false;
        if (!cameraRotation.equals(other.cameraRotation))
            return false;
        if (!sampleRotation.equals(other.sampleRotation))
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        double delta = ((Number) precision).doubleValue();
        if (delta < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(delta))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        EbsdMetadata other = (EbsdMetadata) obj;
        if (Math.abs(beamEnergy - other.beamEnergy) > delta)
            return false;
        if (Math.abs(tiltAngle - other.tiltAngle) > delta)
            return false;
        if (Math.abs(workingDistance - other.workingDistance) > delta)
            return false;
        if (Math.abs(magnification - other.magnification) > delta)
            return false;

        if (!camera.equals(other.camera, precision))
            return false;
        if (!cameraRotation.equals(other.cameraRotation, precision))
            return false;
        if (!sampleRotation.equals(other.sampleRotation, precision))
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(beamEnergy);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((camera == null) ? 0 : camera.hashCode());
        result =
                prime
                        * result
                        + ((cameraRotation == null) ? 0
                                : cameraRotation.hashCode());
        temp = Double.doubleToLongBits(magnification);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result =
                prime
                        * result
                        + ((sampleRotation == null) ? 0
                                : sampleRotation.hashCode());
        temp = Double.doubleToLongBits(tiltAngle);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(workingDistance);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
