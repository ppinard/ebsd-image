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

import static crystallography.core.Constants.*;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ptpshared.core.math.Matrix3D;

public class OperatorTest {

    private static double h2 = 1.0 / 2.0;

    private static double s3 = sqrt(3) / 2;



    @Test
    public void testO1() {
        Matrix3D expected = new Matrix3D(1, 0, 0, 0, 1, 0, 0, 0, 1);
        Matrix3D m = O1.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO2_z() {
        Matrix3D expected = new Matrix3D(-1, 0, 0, 0, -1, 0, 0, 0, 1);
        Matrix3D m = O2_Z.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO2_y() {
        Matrix3D expected = new Matrix3D(-1, 0, 0, 0, 1, 0, 0, 0, -1);
        Matrix3D m = O2_Y.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO2_x() {
        Matrix3D expected = new Matrix3D(1, 0, 0, 0, -1, 0, 0, 0, -1);
        Matrix3D m = O2_X.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO3p_xyz() {
        Matrix3D expected = new Matrix3D(0, 0, 1, 1, 0, 0, 0, 1, 0);
        Matrix3D m = O3P_XYZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO3n_xyz() {
        Matrix3D expected = new Matrix3D(0, 1, 0, 0, 0, 1, 1, 0, 0);
        Matrix3D m = O3N_XYZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO3p_xnynz() {
        Matrix3D expected = new Matrix3D(0, 0, -1, -1, 0, 0, 0, 1, 0);
        Matrix3D m = O3P_XNYNZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO3n_xnynz() {
        Matrix3D expected = new Matrix3D(0, -1, 0, 0, 0, 1, -1, 0, 0);
        Matrix3D m = O3N_XNYNZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO3p_nxynz() {
        Matrix3D expected = new Matrix3D(0, 0, 1, -1, 0, 0, 0, -1, 0);
        Matrix3D m = O3P_NXYNZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO3n_nxynz() {
        Matrix3D expected = new Matrix3D(0, -1, 0, 0, 0, -1, 1, 0, 0);
        Matrix3D m = O3N_NXYNZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO3p_nxnyz() {
        Matrix3D expected = new Matrix3D(0, 0, -1, 1, 0, 0, 0, -1, 0);
        Matrix3D m = O3P_NXNYZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO3n_nxnyz() {
        Matrix3D expected = new Matrix3D(0, 1, 0, 0, 0, -1, -1, 0, 0);
        Matrix3D m = O3N_NXNYZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO2_xy() {
        Matrix3D expected = new Matrix3D(0, 1, 0, 1, 0, 0, 0, 0, -1);
        Matrix3D m = O2_XY.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO2_xny() {
        Matrix3D expected = new Matrix3D(0, -1, 0, -1, 0, 0, 0, 0, -1);
        Matrix3D m = O2_XNY.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO2_xz() {
        Matrix3D expected = new Matrix3D(0, 0, 1, 0, -1, 0, 1, 0, 0);
        Matrix3D m = O2_XZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO2_nxz() {
        Matrix3D expected = new Matrix3D(0, 0, -1, 0, -1, 0, -1, 0, 0);
        Matrix3D m = O2_NXZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO2_yz() {
        Matrix3D expected = new Matrix3D(-1, 0, 0, 0, 0, 1, 0, 1, 0);
        Matrix3D m = O2_YZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO2_ynz() {
        Matrix3D expected = new Matrix3D(-1, 0, 0, 0, 0, -1, 0, -1, 0);
        Matrix3D m = O2_YNZ.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO4p_z() {
        Matrix3D expected = new Matrix3D(0, -1, 0, 1, 0, 0, 0, 0, 1);
        Matrix3D m = O4P_Z.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO4n_z() {
        Matrix3D expected = new Matrix3D(0, 1, 0, -1, 0, 0, 0, 0, 1);
        Matrix3D m = O4N_Z.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO4p_y() {
        Matrix3D expected = new Matrix3D(0, 0, 1, 0, 1, 0, -1, 0, 0);
        Matrix3D m = O4P_Y.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO4n_y() {
        Matrix3D expected = new Matrix3D(0, 0, -1, 0, 1, 0, 1, 0, 0);
        Matrix3D m = O4N_Y.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO4p_x() {
        Matrix3D expected = new Matrix3D(1, 0, 0, 0, 0, -1, 0, 1, 0);
        Matrix3D m = O4P_X.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testO4n_x() {
        Matrix3D expected = new Matrix3D(1, 0, 0, 0, 0, 1, 0, -1, 0);
        Matrix3D m = O4N_X.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testH3p_z() {
        Matrix3D expected = new Matrix3D(-h2, -s3, 0, s3, -h2, 0, 0, 0, 1);
        Matrix3D m = H3P_Z.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testH3n_z() {
        Matrix3D expected = new Matrix3D(-h2, s3, 0, -s3, -h2, 0, 0, 0, 1);
        Matrix3D m = H3N_Z.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testH6p_z() {
        Matrix3D expected = new Matrix3D(h2, -s3, 0, s3, h2, 0, 0, 0, 1);
        Matrix3D m = H6P_Z.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testH6n_z() {
        Matrix3D expected = new Matrix3D(h2, s3, 0, -s3, h2, 0, 0, 0, 1);
        Matrix3D m = H6N_Z.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testH2_xy() {
        Matrix3D expected = new Matrix3D(h2, s3, 0, s3, -h2, 0, 0, 0, -1);
        Matrix3D m = H2_XY.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testH2_xny() {
        Matrix3D expected = new Matrix3D(h2, -s3, 0, -s3, -h2, 0, 0, 0, -1);
        Matrix3D m = H2_XNY.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testH2_x2y() {
        Matrix3D expected = new Matrix3D(-h2, -s3, 0, -s3, h2, 0, 0, 0, -1);
        Matrix3D m = H2_X2Y.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }



    @Test
    public void testH2_2xy() {
        Matrix3D expected = new Matrix3D(-h2, s3, 0, s3, h2, 0, 0, 0, -1);
        Matrix3D m = H2_2XY.toSO3matrix();
        assertTrue(m.equals(expected, 1e-7));
    }

}
