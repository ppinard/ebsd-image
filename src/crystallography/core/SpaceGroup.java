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

import ptpshared.core.math.Quaternion;
import ptpshared.utility.xml.ObjectXml;

/**
 * Crystallographic space group as defined in the International Tables for
 * Crystallography.
 * 
 * @author Philippe T. Pinard
 */
public class SpaceGroup implements ObjectXml {

    /** Crystallographic system. */
    public final CrystalSystem crystalSystem;

    /** Array of generators. */
    private final Generator[] generators;

    /** Index/number of the space group (1 to 203). */
    public final int index;

    /** Laue group. */
    public final LaueGroup laueGroup;

    /** Symbol of the space group. */
    public final String symbol;



    /**
     * Creates a new <code>SpaceGroup</code>.
     * 
     * @param index
     *            index/number of the space group (1 to 203)
     * @param symbol
     *            symbol of the space group
     * @param crystalSystem
     *            crystallographic system
     * @param laueGroup
     *            Laue group
     * @param generators
     *            array of generators
     */
    protected SpaceGroup(int index, String symbol, CrystalSystem crystalSystem,
            LaueGroup laueGroup, Generator[] generators) {
        this.index = index;
        this.symbol = symbol;
        this.crystalSystem = crystalSystem;
        this.laueGroup = laueGroup;
        this.generators = generators.clone();
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        SpaceGroup other = (SpaceGroup) obj;
        if (index != other.index)
            return false;

        return true;
    }



    /**
     * Returns the array of generators associated with the space group.
     * 
     * @return generators
     */
    public Generator[] getGenerators() {
        return generators;
    }



    /**
     * Returns the symmetry operators of the Laue group.
     * 
     * @return symmetry operators
     */
    public Quaternion[] getOperators() {
        return laueGroup.getOperators();
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        return result;
    }



    @Override
    public String toString() {
        return symbol;
    }
}
