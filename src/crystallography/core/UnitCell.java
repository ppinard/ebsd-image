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
package crystallography.core;

import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import ptpshared.core.math.Matrix3D;
import static ptpshared.core.math.Math.acos;
import static ptpshared.core.math.Math.sqrt;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Defines a unit cell. From the lattice parameters and angles, the class
 * calculates for any crystal system:
 * <ul>
 * <li>the reciprocal lattice parameters</li>
 * <li>the reciprocal lattice angles</li>
 * <li>the cell's volume</li>
 * <li>the reciprocal cell's volume</li>
 * <li>the Cartesian matrix</li>
 * <li>the metrical matrix</li>
 * </ul>
 * All these results can be access via public final variables.
 * <b>References:</b>
 * <ul>
 * <li>Mathematical Crystallography</li>
 * <li>International Crystallography Tables</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
@Root
@Immutable
public class UnitCell implements AlmostEquable {
    /** Lattice constant a. */
    @Attribute(name = "a")
    public final double a;

    /** Lattice constant b. */
    @Attribute(name = "b")
    public final double b;

    /** Lattice constant c. */
    @Attribute(name = "c")
    public final double c;

    /** Lattice angle alpha. */
    @Attribute(name = "alpha")
    public final double alpha;

    /** Lattice angle beta. */
    @Attribute(name = "beta")
    public final double beta;

    /** Lattice angle gamma. */
    @Attribute(name = "gamma")
    public final double gamma;

    /** Lattice volume. */
    public final double volume;

    /** Reciprocal lattice constant a. */
    public final double aR;

    /** Reciprocal lattice constant b. */
    public final double bR;

    /** Reciprocal lattice constant c. */
    public final double cR;

    /** Reciprocal lattice angle alpha. */
    public final double alphaR;

    /** Reciprocal lattice angle beta. */
    public final double betaR;

    /** Reciprocal lattice angle gamma. */
    public final double gammaR;

    /** Reciprocal lattice volume. */
    public final double volumeR;

    /** Metrical matrix. */
    public final Matrix3D metricalMatrix;

    /** Cartesian matrix. */
    public final Matrix3D cartesianMatrix;



    /**
     * Creates a new unit cell.
     * 
     * @param a
     *            lattice dimension a (in angstroms)
     * @param b
     *            lattice dimension b (in angstroms)
     * @param c
     *            lattice dimension c (in angstroms)
     * @param alpha
     *            angle between b and c (in radians)
     * @param beta
     *            angle between a and c (in radians)
     * @param gamma
     *            angle between a and b (in radians)
     */
    public UnitCell(@Attribute(name = "a") double a,
            @Attribute(name = "b") double b, @Attribute(name = "c") double c,
            @Attribute(name = "alpha") double alpha,
            @Attribute(name = "beta") double beta,
            @Attribute(name = "gamma") double gamma) {
        if (a <= 0)
            throw new IllegalArgumentException("Lattice constant a (" + a
                    + ") must be greater than 0.");
        if (b <= 0)
            throw new IllegalArgumentException("Lattice constant b (" + b
                    + ") must be greater than 0.");
        if (c <= 0)
            throw new IllegalArgumentException("Lattice constant c (" + c
                    + ") must be greater than 0.");
        if (alpha <= 0 || alpha >= PI)
            throw new IllegalArgumentException("Lattice angle alpha (" + alpha
                    + ") must be between ]0,PI[.");
        if (beta <= 0 || beta >= PI)
            throw new IllegalArgumentException("Lattice angle beta (" + beta
                    + ") must be between ]0,PI[.");
        if (gamma <= 0 || gamma >= PI)
            throw new IllegalArgumentException("Lattice angle gamma (" + gamma
                    + ") must be between ]0,PI[.");

        this.a = a;
        this.b = b;
        this.c = c;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;

        // Calculate reciprocal angles
        alphaR =
                acos((cos(beta) * cos(gamma) - cos(alpha))
                        / (sin(beta) * sin(gamma)));
        betaR =
                acos((cos(alpha) * cos(gamma) - cos(beta))
                        / (sin(alpha) * sin(gamma)));
        gammaR =
                acos((cos(alpha) * cos(beta) - cos(gamma))
                        / (sin(alpha) * sin(beta)));

        // Calculate metrical matrix
        double g11 = a * a;
        double g12 = a * b * cos(gamma);
        double g13 = a * c * cos(beta);
        double g21 = g12;
        double g22 = b * b;
        double g23 = b * c * cos(alpha);
        double g31 = g13;
        double g32 = g23;
        double g33 = c * c;

        metricalMatrix =
                new Matrix3D(g11, g12, g13, g21, g22, g23, g31, g32, g33);

        // Calculate cartesian matrix
        double h11 = a * sin(beta);
        double h12 = -b * sin(alpha) * cos(gammaR);
        double h13 = 0;
        double h21 = 0;
        double h22 = b * sin(alpha) * sin(gammaR);
        double h23 = 0;
        double h31 = a * cos(beta);
        double h32 = b * cos(alpha);
        double h33 = c;

        cartesianMatrix =
                new Matrix3D(h11, h12, h13, h21, h22, h23, h31, h32, h33);

        // Calculate volume
        volume = sqrt(metricalMatrix.det());

        // Calculate reciprocal volume
        volumeR = 1.0 / volume;

        // Calculate reciprocal bases
        aR = b * c * sin(alpha) / volume;
        bR = a * c * sin(beta) / volume;
        cR = a * b * sin(gamma) / volume;
    }



    /**
     * Checks if this <code>UnitCell</code> is exactly equal to the specified
     * one.
     * 
     * @param obj
     *            other <code>UnitCell</code> to check equality
     * @return whether the two <code>UnitCell</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        UnitCell other = (UnitCell) obj;
        if (Double.doubleToLongBits(a) != Double.doubleToLongBits(other.a))
            return false;
        if (Double.doubleToLongBits(alpha) != Double.doubleToLongBits(other.alpha))
            return false;
        if (Double.doubleToLongBits(b) != Double.doubleToLongBits(other.b))
            return false;
        if (Double.doubleToLongBits(beta) != Double.doubleToLongBits(other.beta))
            return false;
        if (Double.doubleToLongBits(c) != Double.doubleToLongBits(other.c))
            return false;
        if (Double.doubleToLongBits(gamma) != Double.doubleToLongBits(other.gamma))
            return false;

        return true;
    }



    /**
     * Checks if this <code>UnitCell</code> is almost equal to the specified one
     * with the given precision.
     * 
     * @param obj
     *            other <code>UnitCell</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>UnitCell</code> are almost equal
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
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

        UnitCell other = (UnitCell) obj;

        if (abs(a - other.a) > delta)
            return false;
        if (abs(b - other.b) > delta)
            return false;
        if (abs(c - other.c) > delta)
            return false;
        if (abs(alpha - other.alpha) > delta)
            return false;
        if (abs(beta - other.beta) > delta)
            return false;
        if (abs(gamma - other.gamma) > delta)
            return false;

        return true;
    }



    /**
     * Returns the hash code for this <code>UnitCell</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(a);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(alpha);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(b);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(beta);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(c);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(gamma);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Returns a representation of the unit cell.
     * 
     * @return information of the <code>UnitCell</code>
     */
    @Override
    public String toString() {
        return "UnitCell [a=" + a + ", b=" + b + ", c=" + c + ", alpha="
                + alpha + " rad, beta=" + beta + " rad, gamma=" + gamma
                + " rad]";
    }

}
