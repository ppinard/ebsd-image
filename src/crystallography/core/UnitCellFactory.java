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
package crystallography.core;

import static java.lang.Math.PI;

/**
 * Factory to create the 7 crystal system's unit cell.
 * 
 * @author Philippe T. Pinard
 */
public class UnitCellFactory {

    /**
     * Returns a cubic unit cell. * a = b = c * alpha = beta = gamma = pi/2
     * 
     * @param a
     *            unit cell parameter in angstroms
     * @return a cubic unit cell
     */
    public static UnitCell cubic(double a) {
        return new UnitCell(a, a, a, PI / 2.0, PI / 2.0, PI / 2.0);
    }



    /**
     * Returns a hexagonal unit cell. * a = b * alpha = beta = pi/2, gamma ==
     * 2pi/3
     * 
     * @param a
     *            unit cell parameter in angstroms
     * @param c
     *            unit cell parameter in angstroms
     * @return a hexagonal unit cell
     */
    public static UnitCell hexagonal(double a, double c) {
        return new UnitCell(a, a, c, PI / 2.0, PI / 2.0, 2.0 * PI / 3.0);
    }



    /**
     * Returns a monoclinic unit cell.
     * 
     * @param a
     *            unit cell parameter in angstroms
     * @param b
     *            unit cell parameter in angstroms
     * @param c
     *            unit cell parameter in angstroms
     * @param beta
     *            unit cell angle in radians
     * @return a monoclinic unit cell
     */
    public static UnitCell monoclinic(double a, double b, double c, double beta) {
        return new UnitCell(a, b, c, PI / 2.0, beta, PI / 2.0);
    }



    /**
     * Returns a orthorhombic unit cell. * a != b != c * alpha = beta = gamma =
     * pi/2
     * 
     * @param a
     *            unit cell parameter in angstroms
     * @param b
     *            unit cell parameter in angstroms
     * @param c
     *            unit cell parameter in angstroms
     * @return a orthorhombic unit cell
     */
    public static UnitCell orthorhombic(double a, double b, double c) {
        return new UnitCell(a, b, c, PI / 2.0, PI / 2.0, PI / 2.0);
    }



    /**
     * Returns a tetragonal unit cell. * a = b * alpha = beta = gamma = pi/2
     * 
     * @param a
     *            unit cell parameter in angstroms
     * @param c
     *            unit cell parameter in angstroms
     * @return a tetragonal unit cell
     */
    public static UnitCell tetragonal(double a, double c) {
        return new UnitCell(a, a, c, PI / 2.0, PI / 2.0, PI / 2.0);
    }



    /**
     * Returns a triclinic unit cell. * a != b != c * alpha != beta != gamma
     * 
     * @param a
     *            unit cell parameter in angstroms
     * @param b
     *            unit cell parameter in angstroms
     * @param c
     *            unit cell parameter in angstroms
     * @param alpha
     *            unit cell angle in radians
     * @param beta
     *            unit cell angle in radians
     * @param gamma
     *            unit cell angle in radians
     * @return a triclinic unit cell
     */
    public static UnitCell triclinic(double a, double b, double c,
            double alpha, double beta, double gamma) {
        return new UnitCell(a, b, c, alpha, beta, gamma);
    }



    /**
     * Returns a trigonal unit cell. * a = b = c * alpha = beta = gamma
     * 
     * @param a
     *            unit cell parameter in angstroms
     * @param alpha
     *            unit cell angle in radians
     * @return a trigonal unit cell
     */
    public static UnitCell trigonal(double a, double alpha) {
        return new UnitCell(a, a, a, alpha, alpha, alpha);
    }
}
