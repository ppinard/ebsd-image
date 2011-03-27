/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
package ptpshared.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FractionsTest {

    @Test
    public void testGCD() {
        assertEquals(Fractions.gcd(12, 18), 6);
        assertEquals(Fractions.gcd(-4, 14), 2);
        assertEquals(Fractions.gcd(9, 28), 1);
        assertEquals(Fractions.gcd(42, 56), 14);
    }

}
