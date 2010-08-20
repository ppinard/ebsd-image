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
import ptpshared.core.math.Matrix3D;
import ptpshared.core.math.Quaternion;
import ptpshared.core.math.Vector3D;

/**
 * Operator: Constants related to symmetry operator and generators for
 * <code>LaueGroup</code> and <code>SpaceGroup</code>.
 * 
 * @author Philippe T. Pinard
 */
public class Constants {
    /** Operator: Identity matrix. */
    public static final Quaternion O1 = Quaternion.IDENTITY;

    /** Operator: 2-fold on [001]. */
    public static final Quaternion O2_Z = new Quaternion(0, 0, 0, 1);

    /** Operator: 2-fold on [010]. */
    public static final Quaternion O2_Y = new Quaternion(0, 0, 1, 0);

    /** Operator: 2-fold on [100]. */
    public static final Quaternion O2_X = new Quaternion(0, 1, 0, 0);

    /** Operator: 3-fold positive on [111]. */
    public static final Quaternion O3P_XYZ = new Quaternion(H2, -H2, -H2, -H2);

    /** Operator: 3-fold negative on [111]. */
    public static final Quaternion O3N_XYZ = new Quaternion(H2, H2, H2, H2);

    /** Operator: 3-fold positive on [1-1-1]. */
    public static final Quaternion O3P_XNYNZ = new Quaternion(H2, -H2, H2, H2);

    /** Operator: 3-fold negative on [1-1-1]. */
    public static final Quaternion O3N_XNYNZ = new Quaternion(H2, H2, -H2, -H2);

    /** Operator: 3-fold positive on [-11-1]. */
    public static final Quaternion O3P_NXYNZ = new Quaternion(H2, H2, -H2, H2);

    /** Operator: 3-fold negative on [-11-1]. */
    public static final Quaternion O3N_NXYNZ = new Quaternion(H2, -H2, H2, -H2);

    /** Operator: 3-fold positive on [-1-11]. */
    public static final Quaternion O3P_NXNYZ = new Quaternion(H2, H2, H2, -H2);

    /** Operator: 3-fold negative on [-1-11]. */
    public static final Quaternion O3N_NXNYZ = new Quaternion(H2, -H2, -H2, H2);

    /** Operator: 2-fold on [110]. */
    public static final Quaternion O2_XY = new Quaternion(0, S2_2, S2_2, 0);

    /** Operator: 2-fold on [1-10]. */
    public static final Quaternion O2_XNY = new Quaternion(0, S2_2, -S2_2, 0);

    /** Operator: 2-fold on [101]. */
    public static final Quaternion O2_XZ = new Quaternion(0, S2_2, 0, S2_2);

    /** Operator: 2-fold on [-101]. */
    public static final Quaternion O2_NXZ = new Quaternion(0, S2_2, 0, -S2_2);

    /** Operator: 2-fold on [011]. */
    public static final Quaternion O2_YZ = new Quaternion(0, 0, S2_2, S2_2);

    /** Operator: 2-fold on [01-1]. */
    public static final Quaternion O2_YNZ = new Quaternion(0, 0, S2_2, -S2_2);

    /** Operator: 4-fold positive on [001]. */
    public static final Quaternion O4P_Z = new Quaternion(S2_2, 0, 0, -S2_2);

    /** Operator: 4-fold negative on [001]. */
    public static final Quaternion O4N_Z = new Quaternion(S2_2, 0, 0, S2_2);

    /** Operator: 4-fold positive on [010]. */
    public static final Quaternion O4P_Y = new Quaternion(S2_2, 0, -S2_2, 0);

    /** Operator: 4-fold negative on [010]. */
    public static final Quaternion O4N_Y = new Quaternion(S2_2, 0, S2_2, 0);

    /** Operator: 4-fold positive on [100]. */
    public static final Quaternion O4P_X = new Quaternion(S2_2, -S2_2, 0, 0);

    /** Operator: 4-fold negative on [100]. */
    public static final Quaternion O4N_X = new Quaternion(S2_2, S2_2, 0, 0);

    /** Operator: 3-fold positive on [001] (hexagonal coordinate system). */
    public static final Quaternion H3P_Z = new Quaternion(H2, 0, 0, -S3_2);

    /** Operator: 3-fold negative on [001] (hexagonal coordinate system). */
    public static final Quaternion H3N_Z = new Quaternion(H2, 0, 0, S3_2);

    /** Operator: 6-fold positive on [001] (hexagonal coordinate system). */
    public static final Quaternion H6P_Z = new Quaternion(S3_2, 0, 0, -H2);

    /** Operator: 6-fold negative on [001] (hexagonal coordinate system). */
    public static final Quaternion H6N_Z = new Quaternion(S3_2, 0, 0, H2);

    /** Operator: 2-fold on [110] (hexagonal coordinate system). */
    public static final Quaternion H2_XY = new Quaternion(0, S3_2, H2, 0);

    /** Operator: 2-fold on [1-10] (hexagonal coordinate system). */
    public static final Quaternion H2_XNY = new Quaternion(0, S3_2, -H2, 0);

    /** Operator: 2-fold on [120] (hexagonal coordinate system). */
    public static final Quaternion H2_X2Y = new Quaternion(0, H2, -S3_2, 0);

    /** Operator: 2-fold on [210] (hexagonal coordinate system). */
    public static final Quaternion H2_2XY = new Quaternion(0, H2, S3_2, 0);

    /** Rotation. */
    public static final Matrix3D ROT_Z_mY_X = new Matrix3D(0.0, 0.0, 1.0, 0.0,
            -1.0, 0.0, 1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_Y_mX_mZ = new Matrix3D(0.0, 1.0, 0.0,
            -1.0, 0.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_XmY_X_mZ = new Matrix3D(1.0, -1.0, 0.0,
            1.0, 0.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mX_Y_mZ = new Matrix3D(-1.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_X_mZ_Y = new Matrix3D(1.0, 0.0, 0.0, 0.0,
            0.0, -1.0, 0.0, 1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_Y_mXY_Z = new Matrix3D(0.0, 1.0, 0.0,
            -1.0, 1.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_Y_mX_Z = new Matrix3D(0.0, 1.0, 0.0, -1.0,
            0.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_XmY_X_Z = new Matrix3D(1.0, -1.0, 0.0,
            1.0, 0.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mX_mXY_mZ = new Matrix3D(-1.0, 0.0, 0.0,
            -1.0, 1.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_Y_Z_X = new Matrix3D(0.0, 1.0, 0.0, 0.0,
            0.0, 1.0, 1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mY_mZ_X = new Matrix3D(0.0, -1.0, 0.0,
            0.0, 0.0, -1.0, 1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_X_Z_mY = new Matrix3D(1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, -1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_XmY_mY_Z = new Matrix3D(1.0, -1.0, 0.0,
            0.0, -1.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_Y_X_mZ = new Matrix3D(0.0, 1.0, 0.0, 1.0,
            0.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_Y_mZ_X = new Matrix3D(0.0, 1.0, 0.0, 0.0,
            0.0, -1.0, 1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mXY_Y_Z = new Matrix3D(-1.0, 1.0, 0.0,
            0.0, 1.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mX_mY_mZ = new Matrix3D(-1.0, 0.0, 0.0,
            0.0, -1.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_X_Y_mZ = new Matrix3D(1.0, 0.0, 0.0, 0.0,
            1.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mXY_mX_Z = new Matrix3D(-1.0, 1.0, 0.0,
            -1.0, 0.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mZ_mY_mX = new Matrix3D(0.0, 0.0, -1.0,
            0.0, -1.0, 0.0, -1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_X_mZ_mY = new Matrix3D(1.0, 0.0, 0.0, 0.0,
            0.0, -1.0, 0.0, -1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_X_Y_Z = new Matrix3D(1.0, 0.0, 0.0, 0.0,
            1.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mY_mX_mZ = new Matrix3D(0.0, -1.0, 0.0,
            -1.0, 0.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mY_X_Z = new Matrix3D(0.0, -1.0, 0.0, 1.0,
            0.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_Z_X_Y = new Matrix3D(0.0, 0.0, 1.0, 1.0,
            0.0, 0.0, 0.0, 1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_X_XmY_Z = new Matrix3D(1.0, 0.0, 0.0, 1.0,
            -1.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mY_X_mZ = new Matrix3D(0.0, -1.0, 0.0,
            1.0, 0.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mY_Z_mX = new Matrix3D(0.0, -1.0, 0.0,
            0.0, 0.0, 1.0, -1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mY_Z_X = new Matrix3D(0.0, -1.0, 0.0, 0.0,
            0.0, 1.0, 1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mX_mZ_mY = new Matrix3D(-1.0, 0.0, 0.0,
            0.0, 0.0, -1.0, 0.0, -1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mX_Z_Y = new Matrix3D(-1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mZ_mX_mY = new Matrix3D(0.0, 0.0, -1.0,
            -1.0, 0.0, 0.0, 0.0, -1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_X_XmY_mZ = new Matrix3D(1.0, 0.0, 0.0,
            1.0, -1.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mY_XmY_mZ = new Matrix3D(0.0, -1.0, 0.0,
            1.0, -1.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_Z_X_mY = new Matrix3D(0.0, 0.0, 1.0, 1.0,
            0.0, 0.0, 0.0, -1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mZ_mY_X = new Matrix3D(0.0, 0.0, -1.0,
            0.0, -1.0, 0.0, 1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_X_Z_Y = new Matrix3D(1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_Z_mX_mY = new Matrix3D(0.0, 0.0, 1.0,
            -1.0, 0.0, 0.0, 0.0, -1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mX_Z_mY = new Matrix3D(-1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0, -1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_X_mY_Z = new Matrix3D(1.0, 0.0, 0.0, 0.0,
            -1.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mY_mX_Z = new Matrix3D(0.0, -1.0, 0.0,
            -1.0, 0.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_Z_mY_mX = new Matrix3D(0.0, 0.0, 1.0, 0.0,
            -1.0, 0.0, -1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mX_mY_Z = new Matrix3D(-1.0, 0.0, 0.0,
            0.0, -1.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_Z_Y_X = new Matrix3D(0.0, 0.0, 1.0, 0.0,
            1.0, 0.0, 1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mZ_Y_mX = new Matrix3D(0.0, 0.0, -1.0,
            0.0, 1.0, 0.0, -1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_Y_Z_mX = new Matrix3D(0.0, 1.0, 0.0, 0.0,
            0.0, 1.0, -1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mY_XmY_Z = new Matrix3D(0.0, -1.0, 0.0,
            1.0, -1.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mXY_Y_mZ = new Matrix3D(-1.0, 1.0, 0.0,
            0.0, 1.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mZ_mX_Y = new Matrix3D(0.0, 0.0, -1.0,
            -1.0, 0.0, 0.0, 0.0, 1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mX_mZ_Y = new Matrix3D(-1.0, 0.0, 0.0,
            0.0, 0.0, -1.0, 0.0, 1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mX_Y_Z = new Matrix3D(-1.0, 0.0, 0.0, 0.0,
            1.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_X_mY_mZ = new Matrix3D(1.0, 0.0, 0.0, 0.0,
            -1.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mZ_X_Y = new Matrix3D(0.0, 0.0, -1.0, 1.0,
            0.0, 0.0, 0.0, 1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_Y_mZ_mX = new Matrix3D(0.0, 1.0, 0.0, 0.0,
            0.0, -1.0, -1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mY_mZ_mX = new Matrix3D(0.0, -1.0, 0.0,
            0.0, 0.0, -1.0, -1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mZ_Y_X = new Matrix3D(0.0, 0.0, -1.0, 0.0,
            1.0, 0.0, 1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_Z_Y_mX = new Matrix3D(0.0, 0.0, 1.0, 0.0,
            1.0, 0.0, -1.0, 0.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mXY_mX_mZ = new Matrix3D(-1.0, 1.0, 0.0,
            -1.0, 0.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_XmY_mY_mZ = new Matrix3D(1.0, -1.0, 0.0,
            0.0, -1.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_Z_mX_Y = new Matrix3D(0.0, 0.0, 1.0, -1.0,
            0.0, 0.0, 0.0, 1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_mX_mXY_Z = new Matrix3D(-1.0, 0.0, 0.0,
            -1.0, 1.0, 0.0, 0.0, 0.0, 1.0);

    /** Rotation. */
    public static final Matrix3D ROT_Y_mXY_mZ = new Matrix3D(0.0, 1.0, 0.0,
            -1.0, 1.0, 0.0, 0.0, 0.0, -1.0);

    /** Rotation. */
    public static final Matrix3D ROT_mZ_X_mY = new Matrix3D(0.0, 0.0, -1.0,
            1.0, 0.0, 0.0, 0.0, -1.0, 0.0);

    /** Rotation. */
    public static final Matrix3D ROT_Y_X_Z = new Matrix3D(0.0, 1.0, 0.0, 1.0,
            0.0, 0.0, 0.0, 0.0, 1.0);

    /** Translation. */
    public static final Vector3D TR_0_0_34 = new Vector3D(0.0, 0.0, 3.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_12_0_34 = new Vector3D(1.0 / 2.0, 0.0,
            3.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_0_0_56 = new Vector3D(0.0, 0.0, 5.0 / 6.0);

    /** Translation. */
    public static final Vector3D TR_12_0_12 = new Vector3D(1.0 / 2.0, 0.0,
            1.0 / 2.0);

    /** Translation. */
    public static final Vector3D TR_0_12_12 = new Vector3D(0.0, 1.0 / 2.0,
            1.0 / 2.0);

    /** Translation. */
    public static final Vector3D TR_12_0_14 = new Vector3D(1.0 / 2.0, 0.0,
            1.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_0_12_14 = new Vector3D(0.0, 1.0 / 2.0,
            1.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_14_14_14 = new Vector3D(1.0 / 4.0,
            1.0 / 4.0, 1.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_0_12_34 = new Vector3D(0.0, 1.0 / 2.0,
            3.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_34_14_14 = new Vector3D(3.0 / 4.0,
            1.0 / 4.0, 1.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_0_0_0 = new Vector3D(0.0, 0.0, 0.0);

    /** Translation. */
    public static final Vector3D TR_23_13_56 = new Vector3D(2.0 / 3.0,
            1.0 / 3.0, 5.0 / 6.0);

    /** Translation. */
    public static final Vector3D TR_14_14_34 = new Vector3D(1.0 / 4.0,
            1.0 / 4.0, 3.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_12_12_0 = new Vector3D(1.0 / 2.0,
            1.0 / 2.0, 0.0);

    /** Translation. */
    public static final Vector3D TR_23_13_13 = new Vector3D(2.0 / 3.0,
            1.0 / 3.0, 1.0 / 3.0);

    /** Translation. */
    public static final Vector3D TR_13_23_23 = new Vector3D(1.0 / 3.0,
            2.0 / 3.0, 2.0 / 3.0);

    /** Translation. */
    public static final Vector3D TR_12_12_12 = new Vector3D(1.0 / 2.0,
            1.0 / 2.0, 1.0 / 2.0);

    /** Translation. */
    public static final Vector3D TR_12_12_14 = new Vector3D(1.0 / 2.0,
            1.0 / 2.0, 1.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_14_34_14 = new Vector3D(1.0 / 4.0,
            3.0 / 4.0, 1.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_12_12_34 = new Vector3D(1.0 / 2.0,
            1.0 / 2.0, 3.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_0_0_23 = new Vector3D(0.0, 0.0, 2.0 / 3.0);

    /** Translation. */
    public static final Vector3D TR_0_12_0 = new Vector3D(0.0, 1.0 / 2.0, 0.0);

    /** Translation. */
    public static final Vector3D TR_14_34_34 = new Vector3D(1.0 / 4.0,
            3.0 / 4.0, 3.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_34_34_14 = new Vector3D(3.0 / 4.0,
            3.0 / 4.0, 1.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_12_0_0 = new Vector3D(1.0 / 2.0, 0.0, 0.0);

    /** Translation. */
    public static final Vector3D TR_34_34_34 = new Vector3D(3.0 / 4.0,
            3.0 / 4.0, 3.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_0_0_13 = new Vector3D(0.0, 0.0, 1.0 / 3.0);

    /** Translation. */
    public static final Vector3D TR_0_0_12 = new Vector3D(0.0, 0.0, 1.0 / 2.0);

    /** Translation. */
    public static final Vector3D TR_13_23_16 = new Vector3D(1.0 / 3.0,
            2.0 / 3.0, 1.0 / 6.0);

    /** Translation. */
    public static final Vector3D TR_0_0_14 = new Vector3D(0.0, 0.0, 1.0 / 4.0);

    /** Translation. */
    public static final Vector3D TR_0_0_16 = new Vector3D(0.0, 0.0, 1.0 / 6.0);

    /** Translation. */
    public static final Vector3D TR_34_14_34 = new Vector3D(3.0 / 4.0,
            1.0 / 4.0, 3.0 / 4.0);
}
