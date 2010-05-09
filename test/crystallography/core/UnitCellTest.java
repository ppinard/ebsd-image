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

import static java.lang.Math.PI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Matrix3D;

public class UnitCellTest {

    private UnitCell unitCell;



    @Before
    public void setUp() {
        unitCell = new UnitCell(1.0, 2.0, 3.0, 1.1, 2.1, 3.1);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(unitCell.equals(unitCell));

        assertFalse(unitCell.equals(null));

        assertFalse(unitCell.equals(new Object()));

        assertFalse(unitCell
                .equals(new UnitCell(99.0, 2.0, 3.0, 1.1, 2.1, 3.1)));

        assertFalse(unitCell
                .equals(new UnitCell(1.0, 99.0, 3.0, 1.1, 2.1, 3.1)));

        assertFalse(unitCell
                .equals(new UnitCell(1.0, 2.0, 99.0, 1.1, 2.1, 3.1)));

        assertFalse(unitCell.equals(new UnitCell(1.0, 2.0, 3.0, 1, 2.1, 3.1)));

        assertFalse(unitCell.equals(new UnitCell(1.0, 2.0, 3.0, 1.1, 2, 3.1)));

        assertFalse(unitCell.equals(new UnitCell(1.0, 2.0, 3.0, 1.1, 2.1, 3)));

        assertTrue(unitCell.equals(new UnitCell(1.0, 2.0, 3.0, 1.1, 2.1, 3.1)));

    }



    @Test
    public void testEqualsUnitCellDouble() {
        assertTrue(unitCell.equals(unitCell, 1e-6));

        assertFalse(unitCell.equals(null, 1e-6));

        assertFalse(unitCell.equals(
                new UnitCell(99.0, 2.0, 3.0, 1.1, 2.1, 3.1), 1e-6));

        assertFalse(unitCell.equals(
                new UnitCell(1.0, 99.0, 3.0, 1.1, 2.1, 3.1), 1e-6));

        assertFalse(unitCell.equals(
                new UnitCell(1.0, 2.0, 99.0, 1.1, 2.1, 3.1), 1e-6));

        assertFalse(unitCell.equals(new UnitCell(1.0, 2.0, 3.0, 1, 2.1, 3.1),
                1e-6));

        assertFalse(unitCell.equals(new UnitCell(1.0, 2.0, 3.0, 1.1, 2, 3.1),
                1e-6));

        assertFalse(unitCell.equals(new UnitCell(1.0, 2.0, 3.0, 1.1, 2.1, 3),
                1e-6));

        assertTrue(unitCell.equals(
                new UnitCell(1.0, 2.0, 3.0002, 1.1, 2.1, 3.1), 1e-3));
    }



    @Test
    public void testToString() {
        String expected =
                "UnitCell [a=1.0, b=2.0, c=3.0, alpha=1.1 rad, beta=2.1 rad, gamma=3.1 rad]";
        assertEquals(expected, unitCell.toString());
    }



    @Test
    public void testUnitCellCartesianMatrix() {
        // Example from Mathematical Crystallography
        double alpha = 93.11 / 180.0 * PI;
        double beta = 115.91 / 180.0 * PI;
        double gamma = 91.26 / 180.0 * PI;

        UnitCell triclinic =
                new UnitCell(8.173, 12.869, 14.165, alpha, beta, gamma);

        Matrix3D cartesianMatrix = triclinic.cartesianMatrix;
        Matrix3D expectedcartesianMatrix =
                new Matrix3D(7.3513, -0.65437, 0.0, 0.0, 12.8333, 0.0, -3.5716,
                        -0.69886, 14.165);
        assertTrue(cartesianMatrix.equals(expectedcartesianMatrix, 1e-2));

        // Identity G = A^T A
        Matrix3D g = cartesianMatrix.transpose().multiply(cartesianMatrix);
        assertTrue(g.equals(triclinic.metricalMatrix, 1e-4));
    }



    @Test
    public void testUnitCellMetricalMatrix() {
        // Example from Mathematical Crystallography
        UnitCell hexagonal = UnitCellFactory.hexagonal(4.914, 5.409);

        Matrix3D expectedMetricalMatrix =
                new Matrix3D(24.1474, -12.0737, 0.0, -12.0737, 24.1474, 0.0,
                        0.0, 0.0, 29.2573);
        assertTrue(hexagonal.metricalMatrix
                .equals(expectedMetricalMatrix, 1e-4));
    }

}
