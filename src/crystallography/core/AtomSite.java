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

import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.apache.commons.math.geometry.Vector3D;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import edu.umd.cs.findbugs.annotations.NonNull;
import static ptpshared.util.ElementProperties.getSymbol;
import static java.lang.Math.abs;

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
@Root(name = "atom")
public class AtomSite implements AlmostEquable {

    /**
     * Refines the position of the atom to be between 0.0 and 1.0.
     * 
     * @param position
     *            position of the atom
     * @return refine position vector
     */
    private static Vector3D refinePosition(Vector3D position) {
        double x = position.getX();
        double y = position.getY();
        double z = position.getZ();

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

    /** Atomic number of the atom. */
    @Attribute(name = "atomicNumber")
    public final int atomicNumber;

    /** Occupancy of the atom on this position. */
    @Attribute(name = "occupancy")
    public final double occupancy;

    /** Position of the atom in the unit cell. */
    @NonNull
    @Element(name = "position")
    public final Vector3D position;



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
        this(atomicNumber, position, 1.0);
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
    public AtomSite(@Attribute(name = "atomicNumber") int atomicNumber,
            @Element(name = "position") Vector3D position,
            @Attribute(name = "occupancy") double occupancy) {
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
     * Checks if this <code>AtomSite</code> is almost equal to the specified one
     * with the given precision.
     * 
     * @param obj
     *            other <code>AtomSite</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>AtomSite</code> are almost equal
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

        AtomSite other = (AtomSite) obj;
        if (atomicNumber != other.atomicNumber)
            return false;

        if (Math.abs(position.getX() - other.position.getX()) > delta)
            return false;
        if (Math.abs(position.getY() - other.position.getY()) > delta)
            return false;
        if (Math.abs(position.getZ() - other.position.getZ()) > delta)
            return false;

        if (abs(occupancy - other.occupancy) > delta)
            return false;

        return true;
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
