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
package org.ebsdimage.core.sim;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.sim.ops.output.OutputOps2Mock;
import org.ebsdimage.core.sim.ops.output.OutputOpsMock;
import org.junit.Test;

public abstract class SimsGeneratorTester {

    protected SimsGenerator generator;



    public static SimsGenerator createSimsGenerator() {
        SimsGenerator generator = new SimsGenerator();

        generator.addItem(1, new OutputOpsMock());
        generator.addItem(2, new OutputOps2Mock());
        generator.addItem(3, new OutputOpsMock());
        generator.addItem(1, new OutputOpsMock());

        return generator;
    }



    @Test
    public void testSimsGenerator() {
        assertEquals(2, generator.getCombinations().size());

        OutputOpsMock op;
        op =
                (OutputOpsMock) generator
                        .getCombinations()
                        .get(0)
                        .get(
                                "0001:"
                                        + "org.ebsdimage.core.sim.ops.output.OutputOpsMock");
        assertEquals("OutputOpsMock []", op.toString());
        op =
                (OutputOpsMock) generator
                        .getCombinations()
                        .get(1)
                        .get(
                                "0001:"
                                        + "org.ebsdimage.core.sim.ops.output.OutputOpsMock");
        assertEquals("OutputOpsMock []", op.toString());
    }



    @Test
    public void testAddItem() {
        generator.addItem(1, new OutputOpsMock());
        assertEquals(3, generator.getCombinations().size());
    }



    @Test
    public void testClearItems() {
        generator.clearItem(1, new OutputOpsMock());
        assertEquals(1, generator.getCombinations().size());
    }



    @Test
    public void testGetCombinations() {
        assertEquals(2, generator.getCombinations().size());
    }



    @Test
    public void testGetKeys() {
        String[] keys = generator.getKeys();
        assertEquals(3, keys.length);
    }

}
