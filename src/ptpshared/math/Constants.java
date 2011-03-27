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
package ptpshared.math;

import static java.lang.Math.sqrt;

/**
 * Physical constants.
 * 
 * @author Philippe T. Pinard
 */
public class Constants {

    /** Planck's constant. */
    public final static double H = 6.62606809633e-34;

    /** Electron's mass. */
    public final static double MASS_ELECTRON = 9.1093818e-31;

    /** Electron's charge. */
    public final static double CHARGE_ELECTRON = 1.60217646e-19;

    /** Speed of light. */
    public final static double C = 2.99792458e8;

    /** Shortcut for 1.0 / 2.0. */
    public static final double H2 = 1.0 / 2.0;

    /** Shortcut for sqrt(2) / 2.0;. */
    public static final double S2_2 = sqrt(2) / 2.0;

    /** Shortcut for sqrt(3) / 2.0. */
    public static final double S3_2 = sqrt(3) / 2.0;

}
