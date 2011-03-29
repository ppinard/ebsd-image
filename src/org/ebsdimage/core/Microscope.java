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

import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.apache.commons.math.geometry.Vector3D;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Version;

import ptpshared.geom.Vector3DUtils;

/**
 * Microscope configuration.
 * 
 * @author Philippe T. Pinard
 */
@Root
@Immutable
@Version(revision = 1)
public class Microscope implements AlmostEquable {

    /** Default microscope configuration. */
    public static final Microscope DEFAULT = new Microscope("Unnamed",
            Camera.NO_CAMERA, new Vector3D(0, 1, 0));

    /** Name of the microscope setup. */
    @Attribute(name = "name")
    public final String name;

    /** Definition of the EBSD camera. */
    @Element(name = "camera")
    public final Camera camera;

    /** Axis along which the sample is tilted. */
    @Element(name = "tiltAxis")
    public final Vector3D tiltAxis;



    /**
     * Creates a new <code>Microscope</code> from the camera and tilt axis
     * information.
     * 
     * @param name
     *            name of the microscope configuration
     * @param camera
     *            camera definition
     * @param tiltAxis
     *            axis along which the sample is tilted
     */
    public Microscope(@Attribute(name = "name") String name,
            @Element(name = "camera") Camera camera,
            @Element(name = "tiltAxis") Vector3D tiltAxis) {
        if (name == null)
            throw new NullPointerException("Name cannot be null.");
        if (name.isEmpty())
            throw new IllegalArgumentException(
                    "Name cannot be an empty string.");
        this.name = name;

        if (camera == null)
            throw new NullPointerException("Camera canont be null.");
        this.camera = camera;

        if (tiltAxis == null)
            throw new NullPointerException("Tilt axis cannot be null.");
        if (tiltAxis.getNorm() == 0.0)
            throw new IllegalArgumentException(
                    "The tilt axis cannot be a null vector.");
        this.tiltAxis = tiltAxis;
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

        Microscope other = (Microscope) obj;
        if (!name.equals(other.name))
            return false;
        if (!camera.equals(other.camera, precision))
            return false;
        if (!Vector3DUtils.equals(tiltAxis, other.tiltAxis, delta))
            return false;

        return true;
    }



    @Override
    public String toString() {
        return name;
    }
}
