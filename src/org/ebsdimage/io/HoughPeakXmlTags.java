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

import org.ebsdimage.core.HoughPeak;

/**
 * Tags for <code>HoughPeak</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class HoughPeakXmlTags {
    /** XML tag name for HoughPeak. */
    public static final String TAG_NAME = HoughPeak.class.getSimpleName();

    /** XML attribute for the rho coordinate. */
    public static final String ATTR_RHO = "rho";

    /** XML attribute for the theta coordinate. */
    public static final String ATTR_THETA = "theta";

    /** XML attribute for theta's units. */
    public static final String ATTR_THETA_UNITS = "thetaUnits";

    /** XML attribute for the maximum intensity of the peak. */
    public static final String ATTR_MAXINTENSITY = "maxintensity";
}
