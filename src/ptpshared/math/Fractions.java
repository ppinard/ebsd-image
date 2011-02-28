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
package ptpshared.math;

/**
 * Operations with fractions.
 * 
 * @author Philippe T. Pinard
 */
public class Fractions {

    /**
     * Finds the greatest common divisor. <b>References:</b>
     * <ul>
     * <li><a href="http://snippets.dzone.com/posts/show/2574">
     * http://snippets.dzone .com/posts/show/2574</a></li>
     * </ul>
     * 
     * @param a
     *            numerator
     * @param b
     *            denominator
     * @return greatest common divisor
     */
    public static int gcd(int a, int b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }

}
