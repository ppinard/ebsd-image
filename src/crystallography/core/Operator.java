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

import static ptpshared.core.math.Constants.H2;
import static ptpshared.core.math.Constants.S2_2;
import static ptpshared.core.math.Constants.S3_2;
import ptpshared.core.math.Quaternion;

/**
 * Symmetry operator for <code>PointGroup</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public enum Operator {
    /** Identity rotation. */
    O1(Quaternion.IDENTITY),

    /** 2-fold on [001]. */
    O2_Z(new Quaternion(0, 0, 0, 1)),

    /** 2-fold on [010]. */
    O2_Y(new Quaternion(0, 0, 1, 0)),

    /** 2-fold on [100]. */
    O2_X(new Quaternion(0, 1, 0, 0)),

    /** 3-fold positive on [111]. */
    O3P_XYZ(new Quaternion(H2, -H2, -H2, -H2)),

    /** 3-fold negative on [111]. */
    O3N_XYZ(new Quaternion(H2, H2, H2, H2)),

    /** 3-fold positive on [1-1-1]. */
    O3P_XNYNZ(new Quaternion(H2, -H2, H2, H2)),

    /** 3-fold negative on [1-1-1]. */
    O3N_XNYNZ(new Quaternion(H2, H2, -H2, -H2)),

    /** 3-fold positive on [-11-1]. */
    O3P_NXYNZ(new Quaternion(H2, H2, -H2, H2)),

    /** 3-fold negative on [-11-1]. */
    O3N_NXYNZ(new Quaternion(H2, -H2, H2, -H2)),

    /** 3-fold positive on [-1-11]. */
    O3P_NXNYZ(new Quaternion(H2, H2, H2, -H2)),

    /** 3-fold negative on [-1-11]. */
    O3N_NXNYZ(new Quaternion(H2, -H2, -H2, H2)),

    /** 2-fold on [110]. */
    O2_XY(new Quaternion(0, S2_2, S2_2, 0)),

    /** 2-fold on [1-10]. */
    O2_XNY(new Quaternion(0, S2_2, -S2_2, 0)),

    /** 2-fold on [101]. */
    O2_XZ(new Quaternion(0, S2_2, 0, S2_2)),

    /** 2-fold on [-101]. */
    O2_NXZ(new Quaternion(0, S2_2, 0, -S2_2)),

    /** 2-fold on [011]. */
    O2_YZ(new Quaternion(0, 0, S2_2, S2_2)),

    /** 2-fold on [01-1]. */
    O2_YNZ(new Quaternion(0, 0, S2_2, -S2_2)),

    /** 4-fold positive on [001]. */
    O4P_Z(new Quaternion(S2_2, 0, 0, -S2_2)),

    /** 4-fold negative on [001]. */
    O4N_Z(new Quaternion(S2_2, 0, 0, S2_2)),

    /** 4-fold positive on [010]. */
    O4P_Y(new Quaternion(S2_2, 0, -S2_2, 0)),

    /** 4-fold negative on [010]. */
    O4N_Y(new Quaternion(S2_2, 0, S2_2, 0)),

    /** 4-fold positive on [100]. */
    O4P_X(new Quaternion(S2_2, -S2_2, 0, 0)),

    /** 4-fold negative on [100]. */
    O4N_X(new Quaternion(S2_2, S2_2, 0, 0)),

    /** 3-fold positive on [001] (hexagonal coordinate system). */
    H3P_Z(new Quaternion(H2, 0, 0, -S3_2)),

    /** 3-fold negative on [001] (hexagonal coordinate system). */
    H3N_Z(new Quaternion(H2, 0, 0, S3_2)),

    /** 6-fold positive on [001] (hexagonal coordinate system). */
    H6P_Z(new Quaternion(S3_2, 0, 0, -H2)),

    /** 6-fold negative on [001] (hexagonal coordinate system). */
    H6N_Z(new Quaternion(S3_2, 0, 0, H2)),

    /** 2-fold on [110] (hexagonal coordinate system). */
    H2_XY(new Quaternion(0, S3_2, H2, 0)),

    /** 2-fold on [1-10] (hexagonal coordinate system). */
    H2_XNY(new Quaternion(0, S3_2, -H2, 0)),

    /** 2-fold on [120] (hexagonal coordinate system). */
    H2_X2Y(new Quaternion(0, H2, -S3_2, 0)),

    /** 2-fold on [210] (hexagonal coordinate system). */
    H2_2XY(new Quaternion(0, H2, S3_2, 0));

    /** Value of the operation (i.e. rotation). */
    public final Quaternion v;



    /**
     * Creates a new operator from its rotation.
     * 
     * @param v
     *            a rotation
     */
    private Operator(Quaternion v) {
        this.v = v;
    }

}
