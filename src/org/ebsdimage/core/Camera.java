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
import magnitude.core.Magnitude;
import net.jcip.annotations.Immutable;

import org.apache.commons.math.geometry.Vector3D;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import ptpshared.geom.Vector3DUtils;
import rmlimage.core.Calibration;

/**
 * Calibration of the EBSD camera.
 * 
 * @author Philippe T. Pinard
 */
@Root
@Immutable
public class Camera implements AlmostEquable {

    /** When no camera is defined. */
    public static final Camera NO_CAMERA = new Camera(new Vector3D(1, 0, 0),
            new Vector3D(0, -1, 0), 0.0, 0.0);

    /** Normal of the camera plane with respect to the microscope CS. */
    @Element(name = "n")
    public final Vector3D n;

    /** Direction of the camera plane with respect to the microscope CS. */
    @Element(name = "x")
    public final Vector3D x;

    /** Width of the camera in unit length (in meters). */
    @Element(name = "width")
    public final double width;

    /** Height of the camera in unit length (in meters). */
    @Element(name = "height")
    public final double height;



    /**
     * Creates a new <code>Camera</code>. A camera is defined by:
     * <ul>
     * <li>a normal vector to the camera's plane
     * <li>a direction vector representing the orientation of the camera with
     * respect to the microscope coordinate system (i.e. rotation around the
     * normal)</li>
     * <li>a translation vector of the camera from the origin of the microscope
     * coordinate system</li>
     * <li>the pixel calibration in the direction parallel and perpendicular to
     * the direction vector</li>
     * </ul>
     * 
     * @param n
     *            normal of the camera plane with respect to the microscope
     *            coordinate system
     * @param x
     *            direction of the camera plane with respect to the microscope
     *            coordinate system
     * @param width
     *            Width of the camera in unit length (in meters).
     * @param height
     *            Height of the camera in unit length (in meters).
     */
    public Camera(@Element(name = "n") Vector3D n,
            @Element(name = "x") Vector3D x,
            @Element(name = "width") double width,
            @Element(name = "height") double height) {
        if (n == null)
            throw new NullPointerException("The normal vector cannot be null.");
        if (n.getNorm() == 0)
            throw new IllegalArgumentException(
                    "The normal of the camera cannot be a null vector.");

        if (x == null)
            throw new NullPointerException(
                    "The direction vector cannot be null.");
        if (x.getNorm() == 0)
            throw new IllegalArgumentException(
                    "The direction of the camera cannot be a null vector.");

        if (Vector3DUtils.areParallel(n, x, 1e-6))
            throw new IllegalArgumentException(
                    "Normal and direction vector cannot be parallel.");

        if (Double.isNaN(width))
            throw new IllegalArgumentException(
                    "The value of the width cannot be NaN.");
        if (Double.isInfinite(width))
            throw new IllegalArgumentException(
                    "The value of the width cannot be infinite.");
        if (width < 0)
            throw new IllegalArgumentException(
                    "Width cannot be less than zero.");

        if (Double.isNaN(height))
            throw new IllegalArgumentException(
                    "The value of the height cannot be NaN.");
        if (Double.isInfinite(height))
            throw new IllegalArgumentException(
                    "The value of the height cannot be infinite.");
        if (height < 0)
            throw new IllegalArgumentException(
                    "Height cannot be less than zero.");

        this.n = n;
        this.x = x;
        this.width = width;
        this.height = height;
    }



    /**
     * Creates a new <code>Camera</code>. A camera is defined by:
     * <ul>
     * <li>a normal vector to the camera's plane
     * <li>a direction vector representing the orientation of the camera with
     * respect to the microscope coordinate system (i.e. rotation around the
     * normal)</li>
     * <li>a translation vector of the camera from the origin of the microscope
     * coordinate system</li>
     * <li>the pixel calibration in the direction parallel and perpendicular to
     * the direction vector</li>
     * </ul>
     * 
     * @param n
     *            normal of the camera plane with respect to the microscope
     *            coordinate system
     * @param x
     *            direction of the camera plane with respect to the microscope
     *            coordinate system
     * @param width
     *            Width of the camera in unit length (in meters).
     * @param height
     *            Height of the camera in unit length (in meters).
     */
    public Camera(Vector3D n, Vector3D x, Magnitude width, Magnitude height) {
        this(n, x, width.getValue("m"), height.getValue("m"));
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

        Camera other = (Camera) obj;
        if (!Vector3DUtils.equals(n, other.n, delta))
            return false;
        if (!Vector3DUtils.equals(x, other.x, delta))
            return false;
        if (Math.abs(width - other.width) > delta)
            return false;
        if (Math.abs(height - other.height) > delta)
            return false;

        return true;
    }



    /**
     * Returns the calibration of the diffraction patterns for this camera based
     * on its resolution.
     * 
     * @param mapWidth
     *            width of the diffraction pattern
     * @param mapHeight
     *            height of the diffraction pattern
     * @return calibration of the diffraction patterns
     */
    public Calibration getCalibration(int mapWidth, int mapHeight) {
        if (mapWidth < 1)
            throw new IllegalArgumentException(
                    "Diffraction pattern's width must be greater or equal to 1.");
        if (mapHeight < 1)
            throw new IllegalArgumentException(
                    "Diffraction pattern's height must be greater or equal to 1.");

        // No camera
        if (width == 0.0 && height == 0.0)
            return Calibration.NONE;

        double mapAspectRatio = (double) mapWidth / (double) mapHeight;
        double cameraAspectRatio = width / height;

        if (Math.abs(mapAspectRatio - cameraAspectRatio) > 1e-6)
            throw new IllegalArgumentException("The aspect ratio of the map ("
                    + mapAspectRatio
                    + ") cannot be different than the aspect ratio of "
                    + "the camera (" + cameraAspectRatio + ").");

        double dx = width / mapWidth;
        return new Calibration(dx, dx, "m");
    }



    /**
     * Returns the height of the camera in unit length (meters).
     * 
     * @return height of the camera
     */
    public Magnitude getHeight() {
        return new Magnitude(height, "m");
    }



    /**
     * Returns the width of the camera in unit length (meters).
     * 
     * @return width of the camera
     */
    public Magnitude getWidth() {
        return new Magnitude(width, "m");
    }

}
