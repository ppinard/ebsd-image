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
package org.ebsdimage.core.exp.ops.pattern.op;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.ebsdimage.core.exp.ops.pattern.op.PatternOp;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;

public class PatternOpMockTest {

    private PatternOp op;



    @Before
    public void setUp() throws Exception {
        op = new PatternOpMock();
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));

        assertFalse(op.equals(null));

        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new PatternOpMock(2)));

        assertTrue(op.equals(new PatternOpMock()));
    }



    @Test
    public void testHashCode() {
        assertEquals(op.hashCode(), new PatternOpMock().hashCode());
    }



    @Test
    public void testLoad() throws IOException {
        ByteMap map = op.load(null);

        assertEquals(2, map.width);
        assertEquals(2, map.height);
        assertEquals(1, map.pixArray[0]);
        assertEquals(2, map.pixArray[1]);
        assertEquals(3, map.pixArray[2]);
        assertEquals(4, map.pixArray[3]);
    }



    @Test
    public void testPatternOpMock() throws IOException {
        assertEquals(PatternOpMock.DEFAULT_INDEX, op.index);
    }



    @Test
    public void testPatternOpMockInt() throws IOException {
        PatternOp other = new PatternOpMock(2);
        assertEquals(2, other.index);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPatternOpMockIntException() {
        new PatternOpMock(-1);
    }

}
