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
package org.ebsdimage.core.exp.ops.hough.pre;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;

public class HoughPreOpsMockTest {

    private HoughPreOps op;

    private ByteMap srcMap;



    @Before
    public void setUp() throws Exception {
        op = new HoughPreOpsMock();
        srcMap = new ByteMap(2, 2, new byte[] { 2, 4, 6, 8 });
    }



    @Test
    public void testProcess() {
        ByteMap result = op.process(null, srcMap);

        assertEquals(2, result.width);
        assertEquals(2, result.height);
        assertEquals(3, result.pixArray[0]);
        assertEquals(5, result.pixArray[1]);
        assertEquals(7, result.pixArray[2]);
        assertEquals(9, result.pixArray[3]);
    }

}
