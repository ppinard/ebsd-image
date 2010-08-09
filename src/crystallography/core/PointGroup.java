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
 * Defines a crystallographic point group. Since in diffraction patterns,
 * centro-symmetric and non centro-symmetric point groups cannot be
 * distinguished, only the non centro-symmetric point groups are defined. Each
 * point group consists of its Schoenflies symbol, its Hermann-Mauguin symbol,
 * its Laue group and an array of all the rotation operations associated with
 * it. <b>References:</b>
 * <ul>
 * <li>International Crystallography Tables</li>
 * <li>Cayron, Cyril. ARPGE, 2010</li>
 * <li>Lecture notes from S. Bohle, McGill University, 2009</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
public enum PointGroup implements ObjectXml, Labeled {

    /** 1st <code>PointGroup</code> (1 or C1). */
    PG1(1, "1", "C1", TRICLINIC, new Operator[] { O1 }),

    /** 2nd <code>PointGroup</code> (2 or C2). */
    PG2(2, "2", "C2", MONOCLINIC, new Operator[] { O1, O2_Y }),

    /** 3rd <code>PointGroup</code> (222 or D2). */
    PG222(3, "222", "D2", ORTHORHOMBIC, new Operator[] { O1, O2_Y, O2_X, O2_Z }),

    /** 10th <code>PointGroup</code> (23 or T). */
    PG23(10, "23", "T", CUBIC, new Operator[] { O1, O3P_XYZ, O3N_XYZ,
            O3P_XNYNZ, O3N_XNYNZ, O3P_NXYNZ, O3N_NXYNZ, O3P_NXNYZ, O3N_NXNYZ,
            O2_Z, O2_Y, O2_X }),

    /** 4th <code>PointGroup</code> (3 or C3). */
    PG3(4, "3", "C3", TRIGONAL, new Operator[] { O1, H3P_Z, H3N_Z }),

    /** 5th <code>PointGroup</code> (32 or D3). */
    PG32(5, "32", "D3", TRIGONAL, new Operator[] { O1, H3P_Z, H3N_Z, H2_XY,
            H2_XNY, O2_Y }),

    /** 6th <code>PointGroup</code> (4 or C4). */
    PG4(6, "4", "C4", TETRAGONAL, new Operator[] { O1, O2_Z, O4P_Z, O4N_Z }),

    /** 7th <code>PointGroup</code> (422 or D4). */
    PG422(7, "422", "D4", TETRAGONAL, new Operator[] { O1, O2_Y, O2_X, O2_Z,
            O4P_Z, O4N_Z, O2_XY, O2_XNY }),

    /** 11th <code>PointGroup</code> (432 or O). */
    PG432(11, "432", "O", CUBIC, new Operator[] { O1, O3P_XYZ, O3N_XYZ,
            O3P_XNYNZ, O3N_XNYNZ, O3P_NXYNZ, O3N_NXYNZ, O3P_NXNYZ, O3N_NXNYZ,
            O2_Z, O2_Y, O2_X, O2_XY, O2_XNY, O2_XZ, O2_NXZ, O2_YZ, O2_YNZ,
            O4P_Z, O4N_Z, O4P_Y, O4N_Y, O4P_X, O4N_X }),

    /** 8th <code>PointGroup</code> (6 or C6). */
    PG6(8, "6", "C6", HEXAGONAL, new Operator[] { O1, H3P_Z, H3N_Z, H6P_Z,
            H6N_Z, O2_Z }),

    /** 9th <code>PointGroup</code> (622 or D6). */
    PG622(9, "622", "D6", HEXAGONAL, new Operator[] { O1, H3P_Z, H3N_Z, H6P_Z,
            H6N_Z, O2_Z, H2_X2Y, H2_2XY, O2_X, H2_XY, O2_Y, H2_XNY });

    /**
     * Returns the <code>PointGroup</code> corresponding to the specified Laue
     * group index.
     * 
     * @param laueGroup
     *            Laue group index from 1 to 11
     * @return a <code>PointGroup</code>
     */
    public static PointGroup fromLaueGroup(int laueGroup) {
        if (laueGroup == 1)
            return PG1;
        else if (laueGroup == 2)
            return PG2;
        else if (laueGroup == 3)
            return PG222;
        else if (laueGroup == 4)
            return PG3;
        else if (laueGroup == 5)
            return PG32;
        else if (laueGroup == 6)
            return PG4;
        else if (laueGroup == 7)
            return PG422;
        else if (laueGroup == 8)
            return PG6;
        else if (laueGroup == 9)
            return PG622;
        else if (laueGroup == 10)
            return PG23;
        else if (laueGroup == 11)
            return PG432;
        else
            throw new IllegalArgumentException("Invalid Laue group ("
                    + laueGroup + "), must be between [1,11].");
    }



    /**
     * Return the point group from the specified space group number (1 to 230).
     * 
     * @param spaceGroup
     *            space group number
     * @return point group of the space group
     */
    public static PointGroup fromSpaceGroup(int spaceGroup) {
        if (spaceGroup >= 1 && spaceGroup <= 2)
            return PG1;
        else if (spaceGroup >= 3 && spaceGroup <= 24)
            return PG2;
        else if (spaceGroup >= 25 && spaceGroup <= 74)
            return PG222;
        else if (spaceGroup >= 75 && spaceGroup <= 88)
            return PG4;
        else if (spaceGroup >= 89 && spaceGroup <= 142)
            return PG422;
        else if (spaceGroup >= 143 && spaceGroup <= 148)
            return PG3;
        else if (spaceGroup >= 149 && spaceGroup <= 167)
            return PG32;
        else if (spaceGroup >= 168 && spaceGroup <= 176)
            return PG6;
        else if (spaceGroup >= 177 && spaceGroup <= 194)
            return PG622;
        else if (spaceGroup >= 195 && spaceGroup <= 206)
            return PG23;
        else if (spaceGroup >= 207 && spaceGroup <= 230)
            return PG432;
        else
            throw new IllegalArgumentException("Invalid space group ("
                    + spaceGroup + "), must be between [1,230].");
    }

    /** Point group symbol as defined by the Schoenflies convention. */
    @NonNull
    public final String schoenfliesSymbol;

    /** Point group symbol as defined by the Hermann-Mauguin convention. */
    @NonNull
    public final String hmSymbol;

    /** Laue group index. */
    public final int laueGroup;

    /** Crystal system. */
    @NonNull
    public final CrystalSystem crystalSystem;

    /** List of symmetry operators associated with the point group. */
    @NonNull
    private final Operator[] operators;



    /**
     * Creates a new <code>PointGroup</code>. Private constructor, the static
     * method should be used instead.
     * 
     * @param schoenfliesSymbol
     *            point group symbol using the Schoenflies convention
     * @param hmSymbol
     *            point group symbol using the Hermann-Mauguin convention
     * @param laueGroup
     *            index of the point group's Laue group
     * @param crystalSystem
     *            crystal system
     * @param operators
     *            list of symmetry operators
     */
    private PointGroup(int laueGroup, String hmSymbol,
            String schoenfliesSymbol, CrystalSystem crystalSystem,
            Operator[] operators) {
        this.schoenfliesSymbol = schoenfliesSymbol;
        this.hmSymbol = hmSymbol;
        this.laueGroup = laueGroup;
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
        return hmSymbol + " / " + schoenfliesSymbol;
    }

}
