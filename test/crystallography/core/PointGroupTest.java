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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import ptpshared.core.math.Quaternion;

public class PointGroupTest {

    @Test
    public void testPG1() {
        PointGroup pg = PointGroup.PG1;
        assertEquals(1, pg.getOperators().length);
        assertEquals("1", pg.hmSymbol);
        assertEquals("C1", pg.schoenfliesSymbol);
        assertEquals(1, pg.laueGroup);
        assertEquals(CrystalSystem.TRICLINIC, pg.crystalSystem);
        allOperationsUnique(pg.getOperators());
    }



    @Test
    public void testPG2() {
        PointGroup pg = PointGroup.PG2;
        assertEquals(2, pg.getOperators().length);
        assertEquals("2", pg.hmSymbol);
        assertEquals("C2", pg.schoenfliesSymbol);
        assertEquals(2, pg.laueGroup);
        assertEquals(CrystalSystem.MONOCLINIC, pg.crystalSystem);
        allOperationsUnique(pg.getOperators());
    }



    @Test
    public void testPG222() {
        PointGroup pg = PointGroup.PG222;
        assertEquals(4, pg.getOperators().length);
        assertEquals("222", pg.hmSymbol);
        assertEquals("D2", pg.schoenfliesSymbol);
        assertEquals(3, pg.laueGroup);
        assertEquals(CrystalSystem.ORTHORHOMBIC, pg.crystalSystem);
        allOperationsUnique(pg.getOperators());
    }



    @Test
    public void testPG3() {
        PointGroup pg = PointGroup.PG3;
        assertEquals(3, pg.getOperators().length);
        assertEquals("3", pg.hmSymbol);
        assertEquals("C3", pg.schoenfliesSymbol);
        assertEquals(4, pg.laueGroup);
        assertEquals(CrystalSystem.TRIGONAL, pg.crystalSystem);
        allOperationsUnique(pg.getOperators());
    }



    @Test
    public void testPG32() {
        PointGroup pg = PointGroup.PG32;
        assertEquals(6, pg.getOperators().length);
        assertEquals("32", pg.hmSymbol);
        assertEquals("D3", pg.schoenfliesSymbol);
        assertEquals(5, pg.laueGroup);
        assertEquals(CrystalSystem.TRIGONAL, pg.crystalSystem);
        allOperationsUnique(pg.getOperators());
    }



    @Test
    public void testPG4() {
        PointGroup pg = PointGroup.PG4;
        assertEquals(4, pg.getOperators().length);
        assertEquals("4", pg.hmSymbol);
        assertEquals("C4", pg.schoenfliesSymbol);
        assertEquals(6, pg.laueGroup);
        assertEquals(CrystalSystem.TETRAGONAL, pg.crystalSystem);
        allOperationsUnique(pg.getOperators());
    }



    @Test
    public void testPG422() {
        PointGroup pg = PointGroup.PG422;
        assertEquals(8, pg.getOperators().length);
        assertEquals("422", pg.hmSymbol);
        assertEquals("D4", pg.schoenfliesSymbol);
        assertEquals(7, pg.laueGroup);
        assertEquals(CrystalSystem.TETRAGONAL, pg.crystalSystem);
        allOperationsUnique(pg.getOperators());
    }



    @Test
    public void testPG6() {
        PointGroup pg = PointGroup.PG6;
        assertEquals(6, pg.getOperators().length);
        assertEquals("6", pg.hmSymbol);
        assertEquals("C6", pg.schoenfliesSymbol);
        assertEquals(8, pg.laueGroup);
        assertEquals(CrystalSystem.HEXAGONAL, pg.crystalSystem);
        allOperationsUnique(pg.getOperators());
    }



    @Test
    public void testPG622() {
        PointGroup pg = PointGroup.PG622;
        assertEquals(12, pg.getOperators().length);
        assertEquals("622", pg.hmSymbol);
        assertEquals("D6", pg.schoenfliesSymbol);
        assertEquals(9, pg.laueGroup);
        assertEquals(CrystalSystem.HEXAGONAL, pg.crystalSystem);
        allOperationsUnique(pg.getOperators());
    }



    @Test
    public void testPG23() {
        PointGroup pg = PointGroup.PG23;
        assertEquals(12, pg.getOperators().length);
        assertEquals("23", pg.hmSymbol);
        assertEquals("T", pg.schoenfliesSymbol);
        assertEquals(10, pg.laueGroup);
        assertEquals(CrystalSystem.CUBIC, pg.crystalSystem);
        allOperationsUnique(pg.getOperators());
    }



    @Test
    public void testPG432() {
        PointGroup pg = PointGroup.PG432;
        assertEquals(24, pg.getOperators().length);
        assertEquals("432", pg.hmSymbol);
        assertEquals("O", pg.schoenfliesSymbol);
        assertEquals(11, pg.laueGroup);
        assertEquals(CrystalSystem.CUBIC, pg.crystalSystem);
        allOperationsUnique(pg.getOperators());
    }



    @Test
    public void testFromLaueGroup() {
        assertEquals(PointGroup.PG1, PointGroup.fromLaueGroup(1));
        assertEquals(PointGroup.PG2, PointGroup.fromLaueGroup(2));
        assertEquals(PointGroup.PG222, PointGroup.fromLaueGroup(3));
        assertEquals(PointGroup.PG3, PointGroup.fromLaueGroup(4));
        assertEquals(PointGroup.PG32, PointGroup.fromLaueGroup(5));
        assertEquals(PointGroup.PG4, PointGroup.fromLaueGroup(6));
        assertEquals(PointGroup.PG422, PointGroup.fromLaueGroup(7));
        assertEquals(PointGroup.PG6, PointGroup.fromLaueGroup(8));
        assertEquals(PointGroup.PG622, PointGroup.fromLaueGroup(9));
        assertEquals(PointGroup.PG23, PointGroup.fromLaueGroup(10));
        assertEquals(PointGroup.PG432, PointGroup.fromLaueGroup(11));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testFromLaueGroupException1() {
        PointGroup.fromLaueGroup(0);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testFromLaueGroupException2() {
        PointGroup.fromLaueGroup(12);
    }



    private void allOperationsUnique(Quaternion[] ops) {
        for (Quaternion op1 : ops) {
            int count = 0;

            for (Quaternion op2 : ops)
                if (op1.equals(op2))
                    count += 1;

            if (count > 1)
                fail("Operation (" + op1 + ") is duplicated.");
        }
    }
}
