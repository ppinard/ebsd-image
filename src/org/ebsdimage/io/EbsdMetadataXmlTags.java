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
package org.ebsdimage.io;

/**
 * Tags for <code>EbsdMetadata</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 */
public abstract class EbsdMetadataXmlTags {

    /** XML tag name for the beam energy. */
    public static final String TAG_BEAM_ENERGY = "BeamEnergy";

    /** XML tag name for the magnification. */
    public static final String TAG_MAGNIFICATION = "Magnification";

    /** XML tag name for the tilt angle. */
    public static final String TAG_TILT_ANGLE = "TiltAngle";

    /** XML tag name for the working distance. */
    public static final String TAG_WORKING_DISTANCE = "WorkingDistance";

    /** XML tag name for the pixel dimension. */
    public static final String TAG_PIXEL_DIMENSION = "PixelDimension";

    /** XML attribute for the pixel width. */
    public static final String ATTR_PIXEL_WIDTH = "width";

    /** XML attribute for the pixel height. */
    public static final String ATTR_PIXEL_HEIGHT = "height";

    /** XML tag name for the sample rotation. */
    public static final String TAG_SAMPLE_ROTATION = "SampleRotation";

    /** XML tag name for the camera's calibration. */
    public static final String TAG_CALIBRATION = "Calibration";
}
