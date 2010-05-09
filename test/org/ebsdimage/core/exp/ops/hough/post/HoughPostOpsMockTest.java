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
package org.ebsdimage.core.exp.ops.hough.post;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.ops.hough.post.HoughPostOps;
import org.junit.Before;
import org.junit.Test;


public class HoughPostOpsMockTest {

    private HoughPostOps op;
    private HoughMap srcMap;



    @Before
    public void setUp() throws Exception {
        op = new HoughPostOpsMock();

        srcMap = new HoughMap(4, 3, 1, 1);
        byte[] srcPixArray = new byte[] { 3, 5, 7, 9 };
        for (int i = 0; i < srcMap.size; i++)
            srcMap.pixArray[i] = srcPixArray[i % 4];
    }



    @Test
    public void testProcess() {
        HoughMap result = op.process(null, srcMap);

        assertEquals(4, result.width);
        assertEquals(3, result.height);
        assertEquals(1.0, result.deltaR, 1e-6);
        assertEquals(1.0, result.deltaTheta, 1e-6);

        byte[] srcPixArray = new byte[] { 6, 10, 14, 18 };
        for (int i = 0; i < result.size; i++)
            assertEquals(srcPixArray[i % 4], result.pixArray[i]);
    }

}
