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

import net.jcip.annotations.Immutable;
import ptpshared.utility.xml.ObjectXml;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Defines a crystal. A crystal consists of:
 * <ul>
 * <li>a {@link UnitCell}</li>
 * <li>an {@link AtomSites}</li>
 * <li>a {@link LaueGroup}</li>
 * </ul>
 * . A name is also associated to the crystal for identification. The name, unit
 * cell, atom sites and Laue group can be accessed via the public final
 * variables <code>name</code>, <code>unitCell</code>, <code>atoms</code> and
 * <code>laueGroup</code> respectively.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class Crystal implements ObjectXml {

    /** Given name of the crystal. */
    @NonNull
    public final String name;

    /** Unit cell of the crystal. */
    @NonNull
    public final UnitCell unitCell;

    /** Atom sites of the crystal. */
    @NonNull
    public final AtomSites atoms;

    /** Laue group of the crystal. */
    @NonNull
    public final LaueGroup laueGroup;



    /**
     * Creates a new crystal from its name, unit cell and atom sites.
     * 
     * @param name
     *            name of the crystal
     * @param unitCell
     *            <code>UnitCell</code> of the crystal
     * @param atomSites
     *            <code>AtomSites</code> of the crystal
     * @param laueGroup
     *            <code>LaueGroup</code> of the crystal
     * @throws NullPointerException
     *             if the name is null
     * @throws NullPointerException
     *             if the unit cell is null
     * @throws NullPointerException
     *             if the atom sites is null
     * @throws NullPointerException
     *             if the Laue group is null
     * @throws IllegalArgumentException
     *             if the name is an empty string
     */
    public Crystal(String name, UnitCell unitCell, AtomSites atomSites,
            LaueGroup laueGroup) {
        if (name == null)
            throw new NullPointerException("The crystal name cannot be null.");
        if (name.length() == 0)
            throw new IllegalArgumentException(
                    "The crystal name cannot be an empty string.");
        if (unitCell == null)
            throw new NullPointerException("The unit cell cannot be null.");
        if (atomSites == null)
            throw new NullPointerException("The atom sites cannot be null.");
        if (laueGroup == null)
            throw new NullPointerException("The Laue group cannot be null.");

        this.name = name;
        this.unitCell = unitCell;
        atoms = atomSites;
        this.laueGroup = laueGroup;
    }



    /**
     * Checks if this <code>Crystal</code> is almost equal to the specified one
     * with the given precision.
     * 
     * @param other
     *            other <code>Crystal</code> to check equality
     * @param precision
     *            level of precision
     * @see AtomSites#equals(AtomSites, double)
     * @see UnitCell#equals(UnitCell, double)
     * @return whether the two <code>Crystal</code> are almost equal
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public boolean equals(Crystal other, double precision) {
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

        if (!(name.equals(other.name)))
            return false;
        if (!(unitCell.equals(other.unitCell, precision)))
            return false;
        if (!(atoms.equals(other.atoms, precision)))
            return false;
        if (!(laueGroup.equals(other.laueGroup)))
            return false;

        return true;
    }



    /**
     * Checks if this <code>Crystal</code> is exactly equal to the specified
     * one.
     * 
     * @param obj
     *            other <code>Crystal</code> to check equality
     * @return whether the two <code>Crystal</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Crystal other = (Crystal) obj;
        if (!name.equals(other.name))
            return false;
        if (!unitCell.equals(other.unitCell))
            return false;
        if (!atoms.equals(other.atoms))
            return false;
        if (!(laueGroup.equals(other.laueGroup)))
            return false;

        return true;
    }



    /**
     * Returns the hash code for this <code>Crystal</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + atoms.hashCode();
        result = prime * result + name.hashCode();
        result = prime * result + unitCell.hashCode();
        result = prime * result + laueGroup.hashCode();
        return result;
    }



    /**
     * Returns a representation of the <code>Crystal</code>, suitable for
     * debugging.
     * 
     * @return information about the <code>Crystal</code>
     */
    @Override
    public String toString() {
        return name;
    }

}
