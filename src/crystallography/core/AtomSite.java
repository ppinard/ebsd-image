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

import static java.lang.Math.abs;
import static ptpshared.utility.ElementProperties.getSymbol;
import net.jcip.annotations.Immutable;
import ptpshared.core.math.Vector3D;
import ptpshared.utility.xml.ObjectXml;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Stores an atom site. An atom site is defined by a position (x, y, z) along
 * the crystal axes (a, b, c) and an atomic number. The position is always
 * refined to be between [0, 1[. The position is saved as a {@link Vector3D} to
 * be used in calculations. The atomic number is between 1 (H) and 111 (Rg). The
 * position and atomic number can be accessed by the public final variables
 * <code>atomicNumber</code> and <code>position</code> respectively.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class AtomSite implements ObjectXml {

    /** Atomic number of the atom. */
    public final int atomicNumber;

    /** Position of the atom in the unit cell. */
    @NonNull
    public final Vector3D position;

    /** Occupancy of the atom on this position. */
    public final double occupancy;



    /**
     * Creates a new <code>AtomSite</code> from the atomic number, positions of
     * an atom along the unit cell axes and occurrence.
     * 
     * @param atomicNumber
     *            atomic number of an atom
     * @param x
     *            position along the a axis
     * @param y
     *            position along the b axis
     * @param z
     *            position along the c axis
     * @param occupancy
     *            occurrence of the atom at this position (=1.0 if the atom is
     *            always there)
     * @throws IllegalArgumentException
     *             if the atomic number is outside [1,111].
     * @throws IllegalArgumentException
     *             if the occupancy is outside [0, 1].
     */
    public AtomSite(int atomicNumber, double x, double y, double z,
            double occupancy) {
        this(atomicNumber, new Vector3D(x, y, z), occupancy);
    }



    /**
     * Creates a new <code>AtomSite</code> from the atomic number and positions
     * of an atom along the unit cell axes. The occurrence is set to 1.0.
     * 
     * @param atomicNumber
     *            atomic number of an atom
     * @param x
     *            position along the a axis
     * @param y
     *            position along the b axis
     * @param z
     *            position along the c axis
     * @throws IllegalArgumentException
     *             if the atomic number is outside [1,111].
     */
    public AtomSite(int atomicNumber, double x, double y, double z) {
        this(atomicNumber, new Vector3D(x, y, z));
    }



    /**
     * Creates a new <code>AtomSite</code> from the atomic number, position of
     * an atom and occurrence.
     * 
     * @param atomicNumber
     *            atomic number of an atom
     * @param position
     *            position relative to the unit cell
     * @param occupancy
     *            occurrence of the atom at this position (=1.0 if the atom is
     *            always there)
     * @throws IllegalArgumentException
     *             if the atomic number is outside [1,111].
     * @throws IllegalArgumentException
     *             if the occupancy is outside [0, 1].
     * @throws NullPointerException
     *             if the position vector is null.
     */
    public AtomSite(int atomicNumber, Vector3D position, double occupancy) {
        if (atomicNumber < 1 || atomicNumber > 111)
            throw new IllegalArgumentException("The atomic number ("
                    + atomicNumber
                    + ") is invalid. It must be betwwen [1, 111].");
        if (position == null)
            throw new NullPointerException("The position can not be null.");
        if (occupancy < 0 || occupancy > 1)
            throw new IllegalArgumentException("The occurence (" + occupancy
                    + ") must be between [0,1].");

        this.atomicNumber = atomicNumber;
        this.position = refinePosition(position);
        this.occupancy = occupancy;
    }



    /**
     * Creates a new <code>AtomSite</code> from the atomic number and position
     * of an atom. The occurrence is set to 1.0.
     * 
     * @param atomicNumber
     *            atomic number of an atom
     * @param position
     *            position relative to the unit cell
     * @throws IllegalArgumentException
     *             if the atomic number is outside [1,111].
     * @throws NullPointerException
     *             if the position vector is null.
     */
    public AtomSite(int atomicNumber, Vector3D position) {
        if (atomicNumber < 1 || atomicNumber > 111)
            throw new IllegalArgumentException("The atomic number ("
                    + atomicNumber
                    + ") is invalid. It must be betwwen [1, 111].");
        if (position == null)
            throw new NullPointerException("The position can not be null.");

        this.atomicNumber = atomicNumber;
        this.position = refinePosition(position);
        occupancy = 1.0;
    }



    /**
     * Checks if this <code>AtomSite</code> is almost equal to the specified one
     * with the given precision.
     * 
     * @param other
     *            other <code>AtomSite</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>AtomSite</code> are almost equal
     * 
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public boolean equals(AtomSite other, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(precision))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (this == other)
            return true;
        if (other == null)
            return false;

        if (atomicNumber != other.atomicNumber)
            return false;
        if (!position.equals(other.position, precision))
            return false;
        if (abs(occupancy - other.occupancy) >= precision)
            return false;

        return true;
    }



    /**
     * Checks if this <code>AtomSite</code> is exactly equal to the specified
     * one.
     * 
     * @param obj
     *            other <code>AtomSite</code> to check equality
     * 
     * @return whether the two <code>AtomSite</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        AtomSite other = (AtomSite) obj;
        if (atomicNumber != other.atomicNumber)
            return false;
        if (!position.equals(other.position))
            return false;
        if (Double.doubleToLongBits(occupancy) != Double
                .doubleToLongBits(other.occupancy))
            return false;

        return true;
    }



    /**
     * Returns the hash code for this <code>AtomSite</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;

        result = prime * result + atomicNumber;
        result = prime * result + position.hashCode();
        temp = Double.doubleToLongBits(occupancy);
        result = prime * result + (int) (temp ^ (temp >>> 32));

        return result;
    }



    /**
     * Refines the position of the atom to be between 0.0 and 1.0.
     * 
     * @param position
     *            position of the atom
     * @return refine position vector
     */
    private Vector3D refinePosition(Vector3D position) {
        double x = position.get(0);
        double y = position.get(1);
        double z = position.get(2);

        while (x < 0)
            x += 1;
        while (y < 0)
            y += 1;
        while (z < 0)
            z += 1;

        while (x >= 1)
            x -= 1;
        while (y >= 1)
            y -= 1;
        while (z >= 1)
            z -= 1;

        return new Vector3D(x, y, z);
    }



    /**
     * Returns a representation of the <code>AtomSite</code> , suitable for
     * debugging.
     * 
     * @return information about the <code>AtomSite</code>
     */
    @Override
    public String toString() {
        String symbol = getSymbol(atomicNumber);
        String vector = position.toString();

        return symbol + "->" + vector + " [" + (int) (occupancy * 100) + "%]";
    }

}
