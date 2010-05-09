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
package ptpshared.core.math;

import static java.lang.Math.PI;

/**
 * Common mathematical operations.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class Math {

    /**
     * Overrides {@link java.lang.Math#acos(double)} to prevent rounding errors
     * when the <code>angle</code> is slightly greater than 1.0 or less than
     * -1.0.
     * 
     * @param angle
     *            in radians
     * @return a value between 0.0 and PI.
     */
    public static double acos(double angle) {
        if (Double.isNaN(angle))
            throw new IllegalArgumentException("NaN cannot be evaluated");

        if (angle > 1.0)
            return 0.0;
        else if (angle < -1.0)
            return PI;
        else
            return java.lang.Math.acos(angle);
    }



    /**
     * Overrides {@link java.lang.Math#sqrt(double)} to prevent rounding errors
     * when the <code>value</code> is slightly less than 0.0.
     * 
     * @param value
     *            a double value
     * @return the square root value
     */
    public static double sqrt(double value) {
        if (Double.isNaN(value))
            throw new IllegalArgumentException("NaN cannot be evaluated");

        if (value < 0.0)
            return 0.0;
        else
            return java.lang.Math.sqrt(value);
    }



    /**
     * Returns the sign of the specified double.
     * 
     * @param value
     *            a double
     * @return -1 for negative double, 1 for positive double, 0 otherwise
     */
    public static int sign(double value) {
        if (Double.isNaN(value))
            throw new IllegalArgumentException("NaN cannot be evaluated");

        if (value > 0)
            return 1;
        else if (value < 0)
            return -1;
        else
            return 0;
    }
}
