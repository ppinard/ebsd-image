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
package ptpshared.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ptpshared.util.ElementProperties;

public class ElementPropertiesTest {

    @Test
    public void getAtomicNumber() {
        assertEquals(ElementProperties.getAtomicNumber("Al"), 13);
        assertEquals(ElementProperties.getAtomicNumber("AL"), 13);
        assertEquals(ElementProperties.getAtomicNumber("al"), 13);
    }



    @Test(expected = IllegalArgumentException.class)
    public void getAtomicNumberException() {
        ElementProperties.getAtomicNumber("A");
    }



    @Test
    public void getSymbol() {
        assertEquals(ElementProperties.getSymbol(13), "Al");
    }



    @Test(expected = IllegalArgumentException.class)
    public void getSymbolException1() {
        ElementProperties.getSymbol(0);
    }



    @Test(expected = IllegalArgumentException.class)
    public void getSymbolException2() {
        ElementProperties.getSymbol(110);
    }



    @Test
    public void isCorrect() {
        assertTrue(ElementProperties.isCorrect("H"));
        assertTrue(ElementProperties.isCorrect("h"));

        assertTrue(ElementProperties.isCorrect("Pt"));
        assertTrue(ElementProperties.isCorrect("PT"));
        assertTrue(ElementProperties.isCorrect("pt"));
        assertTrue(ElementProperties.isCorrect("pT"));

        assertFalse(ElementProperties.isCorrect("ptp"));
        assertFalse(ElementProperties.isCorrect("RML"));
    }

}
