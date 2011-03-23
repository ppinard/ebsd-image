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
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import rmlshared.util.Labeled;
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
@Root
@Immutable
public class Crystal implements AlmostEquable, Labeled {

    /** Given name of the crystal. */
    @NonNull
    @Attribute(name = "name")
    public final String name;

    /** Unit cell of the crystal. */
    @NonNull
    @Element(name = "unitCell")
    public final UnitCell unitCell;

    /** Atom sites of the crystal. */
    @NonNull
    @ElementList(name = "atoms", type = AtomSite.class)
    public final AtomSites atoms;

    /** Space group of the crystal. */
    @NonNull
    @Element(name = "spaceGroup")
    public final SpaceGroup spaceGroup;



    /**
     * Creates a new crystal from its name, unit cell and atom sites.
     * 
     * @param name
     *            name of the crystal
     * @param unitCell
     *            <code>UnitCell</code> of the crystal
     * @param atoms
     *            <code>AtomSites</code> of the crystal
     * @param spaceGroup
     *            <code>SpaceGroup</code> of the crystal
     * @throws NullPointerException
     *             if the name is null
     * @throws NullPointerException
     *             if the unit cell is null
     * @throws NullPointerException
     *             if the atom sites is null
     * @throws NullPointerException
     *             if the space group is null
     * @throws IllegalArgumentException
     *             if the name is an empty string
     */
    public Crystal(
            @Attribute(name = "name") String name,
            @Element(name = "unitCell") UnitCell unitCell,
            @ElementList(name = "atoms", type = AtomSite.class) AtomSites atoms,
            @Element(name = "spaceGroup") SpaceGroup spaceGroup) {
        if (name == null)
            throw new NullPointerException("The crystal name cannot be null.");
        if (name.length() == 0)
            throw new IllegalArgumentException(
                    "The crystal name cannot be an empty string.");
        if (unitCell == null)
            throw new NullPointerException("The unit cell cannot be null.");
        if (atoms == null)
            throw new NullPointerException("The atom sites cannot be null.");
        if (spaceGroup == null)
            throw new NullPointerException("The space group cannot be null.");

        this.name = name;
        this.unitCell = unitCell;
        this.atoms = Calculations.equivalentPositions(atoms, spaceGroup);
        this.spaceGroup = spaceGroup;
    }



    /**
     * Checks if this <code>Crystal</code> is almost equal to the specified one
     * with the given precision.
     * 
     * @param obj
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
    @Override
    public boolean equals(Object obj, Object precision) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Crystal other = (Crystal) obj;

        if (!(name.equals(other.name)))
            return false;
        if (!(unitCell.equals(other.unitCell, precision)))
            return false;
        if (!(atoms.equals(other.atoms, precision)))
            return false;
        if (!(spaceGroup.equals(other.spaceGroup)))
            return false;

        return true;
    }



    @Override
    public String getLabel() {
        return name;
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
