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

import org.junit.Test;

import static ptpshared.geom.Assert.assertEquals;
import static crystallography.core.Constants.*;
import static java.lang.Math.sqrt;

public class OperatorTest {

    private static double h2 = 1.0 / 2.0;

    private static double s3 = sqrt(3) / 2;



    @Test
    public void testO1() {
        double[][] expected =
                new double[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
        double[][] m = O1.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO2_z() {
        double[][] expected =
                new double[][] { { -1, 0, 0 }, { 0, -1, 0 }, { 0, 0, 1 } };
        double[][] m = O2_Z.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO2_y() {
        double[][] expected =
                new double[][] { { -1, 0, 0 }, { 0, 1, 0 }, { 0, 0, -1 } };
        double[][] m = O2_Y.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO2_x() {
        double[][] expected =
                new double[][] { { 1, 0, 0 }, { 0, -1, 0 }, { 0, 0, -1 } };
        double[][] m = O2_X.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO3p_xyz() {
        double[][] expected =
                new double[][] { { 0, 0, 1 }, { 1, 0, 0 }, { 0, 1, 0 } };
        double[][] m = O3P_XYZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO3n_xyz() {
        double[][] expected =
                new double[][] { { 0, 1, 0 }, { 0, 0, 1 }, { 1, 0, 0 } };
        double[][] m = O3N_XYZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO3p_xnynz() {
        double[][] expected =
                new double[][] { { 0, 0, -1 }, { -1, 0, 0 }, { 0, 1, 0 } };
        double[][] m = O3P_XNYNZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO3n_xnynz() {
        double[][] expected =
                new double[][] { { 0, -1, 0 }, { 0, 0, 1 }, { -1, 0, 0 } };
        double[][] m = O3N_XNYNZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO3p_nxynz() {
        double[][] expected =
                new double[][] { { 0, 0, 1 }, { -1, 0, 0 }, { 0, -1, 0 } };
        double[][] m = O3P_NXYNZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO3n_nxynz() {
        double[][] expected =
                new double[][] { { 0, -1, 0 }, { 0, 0, -1 }, { 1, 0, 0 } };
        double[][] m = O3N_NXYNZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO3p_nxnyz() {
        double[][] expected =
                new double[][] { { 0, 0, -1 }, { 1, 0, 0 }, { 0, -1, 0 } };
        double[][] m = O3P_NXNYZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO3n_nxnyz() {
        double[][] expected =
                new double[][] { { 0, 1, 0 }, { 0, 0, -1 }, { -1, 0, 0 } };
        double[][] m = O3N_NXNYZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO2_xy() {
        double[][] expected =
                new double[][] { { 0, 1, 0 }, { 1, 0, 0 }, { 0, 0, -1 } };
        double[][] m = O2_XY.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO2_xny() {
        double[][] expected =
                new double[][] { { 0, -1, 0 }, { -1, 0, 0 }, { 0, 0, -1 } };
        double[][] m = O2_XNY.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO2_xz() {
        double[][] expected =
                new double[][] { { 0, 0, 1 }, { 0, -1, 0 }, { 1, 0, 0 } };
        double[][] m = O2_XZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO2_nxz() {
        double[][] expected =
                new double[][] { { 0, 0, -1 }, { 0, -1, 0 }, { -1, 0, 0 } };
        double[][] m = O2_NXZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO2_yz() {
        double[][] expected =
                new double[][] { { -1, 0, 0 }, { 0, 0, 1 }, { 0, 1, 0 } };
        double[][] m = O2_YZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO2_ynz() {
        double[][] expected =
                new double[][] { { -1, 0, 0 }, { 0, 0, -1 }, { 0, -1, 0 } };
        double[][] m = O2_YNZ.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO4p_z() {
        double[][] expected =
                new double[][] { { 0, -1, 0 }, { 1, 0, 0 }, { 0, 0, 1 } };
        double[][] m = O4P_Z.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO4n_z() {
        double[][] expected =
                new double[][] { { 0, 1, 0 }, { -1, 0, 0 }, { 0, 0, 1 } };
        double[][] m = O4N_Z.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO4p_y() {
        double[][] expected =
                new double[][] { { 0, 0, 1 }, { 0, 1, 0 }, { -1, 0, 0 } };
        double[][] m = O4P_Y.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO4n_y() {
        double[][] expected =
                new double[][] { { 0, 0, -1 }, { 0, 1, 0 }, { 1, 0, 0 } };
        double[][] m = O4N_Y.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO4p_x() {
        double[][] expected =
                new double[][] { { 1, 0, 0 }, { 0, 0, -1 }, { 0, 1, 0 } };
        double[][] m = O4P_X.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testO4n_x() {
        double[][] expected =
                new double[][] { { 1, 0, 0 }, { 0, 0, 1 }, { 0, -1, 0 } };
        double[][] m = O4N_X.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testH3p_z() {
        double[][] expected =
                new double[][] { { -h2, -s3, 0 }, { s3, -h2, 0 }, { 0, 0, 1 } };
        double[][] m = H3P_Z.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testH3n_z() {
        double[][] expected =
                new double[][] { { -h2, s3, 0 }, { -s3, -h2, 0 }, { 0, 0, 1 } };
        double[][] m = H3N_Z.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testH6p_z() {
        double[][] expected =
                new double[][] { { h2, -s3, 0 }, { s3, h2, 0 }, { 0, 0, 1 } };
        double[][] m = H6P_Z.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testH6n_z() {
        double[][] expected =
                new double[][] { { h2, s3, 0 }, { -s3, h2, 0 }, { 0, 0, 1 } };
        double[][] m = H6N_Z.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testH2_xy() {
        double[][] expected =
                new double[][] { { h2, s3, 0 }, { s3, -h2, 0 }, { 0, 0, -1 } };
        double[][] m = H2_XY.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testH2_xny() {
        double[][] expected =
                new double[][] { { h2, -s3, 0 }, { -s3, -h2, 0 }, { 0, 0, -1 } };
        double[][] m = H2_XNY.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testH2_x2y() {
        double[][] expected =
                new double[][] { { -h2, -s3, 0 }, { -s3, h2, 0 }, { 0, 0, -1 } };
        double[][] m = H2_X2Y.getMatrix();
        assertEquals(expected, m, 1e-7);
    }



    @Test
    public void testH2_2xy() {
        double[][] expected =
                new double[][] { { -h2, s3, 0 }, { s3, h2, 0 }, { 0, 0, -1 } };
        double[][] m = H2_2XY.getMatrix();
        assertEquals(expected, m, 1e-7);
    }
}
