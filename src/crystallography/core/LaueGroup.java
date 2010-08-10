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

import static crystallography.core.CrystalSystem.*;
import static crystallography.core.Operator.*;
import ptpshared.core.math.Quaternion;
import ptpshared.utility.xml.ObjectXml;
import rmlshared.util.Labeled;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Defines a crystallographic Laue group. Since in diffraction patterns,
 * centro-symmetric and non centro-symmetric point groups cannot be
 * distinguished. Therefore only the Laue group is required. Each Laue group
 * consists of its Schoenflies symbol, its Hermann-Mauguin symbol and an array
 * of all the rotation operations associated with it. <b>References:</b>
 * <ul>
 * <li>International Crystallography Tables</li>
 * <li>Cayron, Cyril. ARPGE, 2010</li>
 * <li>Lecture notes from S. Bohle, McGill University, 2009</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
public enum LaueGroup implements ObjectXml, Labeled {

    /** 1st <code>LaueGroup</code> (1 or -1). */
    LG1(1, "1", TRICLINIC, new Operator[] { O1 }),

    /** 2nd <code>LaueGroup</code> (2, m or 2/m). */
    LG2m(2, "2/m", MONOCLINIC, new Operator[] { O1, O2_Y }),

    /** 3rd <code>LaueGroup</code> (222, mm2 or mmm). */
    LGmmm(3, "mmm", ORTHORHOMBIC, new Operator[] { O1, O2_Y, O2_X, O2_Z }),

    /** 10th <code>LaueGroup</code> (23 or m-3). */
    LGm3(10, "m3", CUBIC, new Operator[] { O1, O3P_XYZ, O3N_XYZ, O3P_XNYNZ,
            O3N_XNYNZ, O3P_NXYNZ, O3N_NXYNZ, O3P_NXNYZ, O3N_NXNYZ, O2_Z, O2_Y,
            O2_X }),

    /** 4th <code>LaueGroup</code> (3 or -3). */
    LG3(4, "3", TRIGONAL, new Operator[] { O1, H3P_Z, H3N_Z }),

    /** 5th <code>LaueGroup</code> (32, 3m or -3m). */
    LG3m(5, "3m", TRIGONAL, new Operator[] { O1, H3P_Z, H3N_Z, H2_XY, H2_XNY,
            O2_Y }),

    /** 6th <code>LaueGroup</code> (4, -4 or 4/m). */
    LG4m(6, "4/m", TETRAGONAL, new Operator[] { O1, O2_Z, O4P_Z, O4N_Z }),

    /** 7th <code>LaueGroup</code> (422, 4mm, 42m or 4/mmm). */
    LG4mmm(7, "4/mmm", TETRAGONAL, new Operator[] { O1, O2_Y, O2_X, O2_Z,
            O4P_Z, O4N_Z, O2_XY, O2_XNY }),

    /** 11th <code>LaueGroup</code> (432, 43m or m3m). */
    LGm3m(11, "m3m", CUBIC, new Operator[] { O1, O3P_XYZ, O3N_XYZ, O3P_XNYNZ,
            O3N_XNYNZ, O3P_NXYNZ, O3N_NXYNZ, O3P_NXNYZ, O3N_NXNYZ, O2_Z, O2_Y,
            O2_X, O2_XY, O2_XNY, O2_XZ, O2_NXZ, O2_YZ, O2_YNZ, O4P_Z, O4N_Z,
            O4P_Y, O4N_Y, O4P_X, O4N_X }),

    /** 8th <code>LaueGroup</code> (6, -6 or 6/m). */
    LG6m(8, "6/m", HEXAGONAL, new Operator[] { O1, H3P_Z, H3N_Z, H6P_Z, H6N_Z,
            O2_Z }),

    /** 9th <code>LaueGroup</code> (622, 6mm, 62m or 6/mmm). */
    LG6mmm(9, "6/mmm", HEXAGONAL, new Operator[] { O1, H3P_Z, H3N_Z, H6P_Z,
            H6N_Z, O2_Z, H2_X2Y, H2_2XY, O2_X, H2_XY, O2_Y, H2_XNY });

    /**
     * Returns the <code>LaueGroup</code> corresponding to the specified Laue
     * group index.
     * 
     * @param laueGroup
     *            Laue group index from 1 to 11
     * @return a <code>LaueGroup</code>
     */
    public static LaueGroup fromIndex(int laueGroup) {
        if (laueGroup == 1)
            return LG1;
        else if (laueGroup == 2)
            return LG2m;
        else if (laueGroup == 3)
            return LGmmm;
        else if (laueGroup == 4)
            return LG3;
        else if (laueGroup == 5)
            return LG3m;
        else if (laueGroup == 6)
            return LG4m;
        else if (laueGroup == 7)
            return LG4mmm;
        else if (laueGroup == 8)
            return LG6m;
        else if (laueGroup == 9)
            return LG6mmm;
        else if (laueGroup == 10)
            return LGm3;
        else if (laueGroup == 11)
            return LGm3m;
        else
            throw new IllegalArgumentException("Invalid Laue group ("
                    + laueGroup + "), must be between [1,11].");
    }



    /**
     * Return the Laue group from the specified space group number (1 to 230).
     * 
     * @param spaceGroup
     *            space group number
     * @return point group of the space group
     */
    public static LaueGroup fromSpaceGroup(int spaceGroup) {
        if (spaceGroup >= 1 && spaceGroup <= 2)
            return LG1;
        else if (spaceGroup >= 3 && spaceGroup <= 24)
            return LG2m;
        else if (spaceGroup >= 25 && spaceGroup <= 74)
            return LGmmm;
        else if (spaceGroup >= 75 && spaceGroup <= 88)
            return LG4m;
        else if (spaceGroup >= 89 && spaceGroup <= 142)
            return LG4mmm;
        else if (spaceGroup >= 143 && spaceGroup <= 148)
            return LG3;
        else if (spaceGroup >= 149 && spaceGroup <= 167)
            return LG3m;
        else if (spaceGroup >= 168 && spaceGroup <= 176)
            return LG6m;
        else if (spaceGroup >= 177 && spaceGroup <= 194)
            return LG6mmm;
        else if (spaceGroup >= 195 && spaceGroup <= 206)
            return LGm3;
        else if (spaceGroup >= 207 && spaceGroup <= 230)
            return LGm3m;
        else
            throw new IllegalArgumentException("Invalid space group ("
                    + spaceGroup + "), must be between [1,230].");
    }

    /** Laue group symbol. */
    @NonNull
    public final String symbol;

    /** Laue group index. */
    public final int index;

    /** Crystal system. */
    @NonNull
    public final CrystalSystem crystalSystem;

    /** List of symmetry operators associated with the point group. */
    @NonNull
    private final Operator[] operators;



    /**
     * Creates a new <code>LaueGroup</code>. Private constructor, the static
     * method should be used instead.
     * 
     * @param index
     *            index of the Laue group
     * @param symbol
     *            Laue group symbol
     * @param crystalSystem
     *            crystal system
     * @param operators
     *            list of symmetry operators
     */
    private LaueGroup(int index, String symbol, CrystalSystem crystalSystem,
            Operator[] operators) {
        this.index = index;
        this.symbol = symbol;
        this.crystalSystem = crystalSystem;
        this.operators = operators;
    }



    /**
     * Returns the array of symmetry operators (i.e. rotation) associated with
     * this point group.
     * 
     * @return list of symmetry operators
     */
    public Quaternion[] getOperators() {
        Quaternion[] rotations = new Quaternion[operators.length];

        for (int i = 0; i < rotations.length; i++)
            rotations[i] = operators[i].v;

        return rotations;
    }



    @Override
    public String getLabel() {
        return symbol;
    }

}
