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
package org.ebsdimage.io;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RplFileTest extends TestCase {

    private RplFile rpl;



    @Before
    public void setUp() throws Exception {
        rpl = new RplFile(1, 168, 128, "dont-care", "unsigned");
    }



    @Test
    public void testRplFile() {
        assertEquals(1, rpl.dataLength);
        assertEquals(168, rpl.width);
        assertEquals(128, rpl.height);
        assertTrue(rpl.isBigEndian());
        assertFalse(rpl.isSigned());
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRplFileException1() {
        new RplFile(3, 168, 128, "dont-care", "unsigned");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRplFileException2() {
        new RplFile(1, 0, 128, "dont-care", "unsigned");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRplFileException3() {
        new RplFile(1, 168, 0, "dont-care", "unsigned");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRplFileException4() {
        new RplFile(1, 168, 128, "", "unsigned");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRplFileException5() {
        new RplFile(1, 168, 128, "dont-care", "");
    }

}
