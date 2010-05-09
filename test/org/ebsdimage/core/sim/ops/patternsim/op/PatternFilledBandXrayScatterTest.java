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
package org.ebsdimage.core.sim.ops.patternsim.op;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimTester;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;

public class PatternFilledBandXrayScatterTest extends TestCase {

    private PatternFilledBandXrayScatter patternOp;



    @Before
    public void setUp() throws Exception {
        patternOp = new PatternFilledBandXrayScatter(336, 256, 5);
    }



    @Test
    public void testSimulate() throws IOException {
        // Create simulation
        Sim sim = SimTester.createSim(new Operation[] { patternOp });

        // Run
        sim.run();

        ByteMap patternMap = sim.getPatternSimOp().getPattern().getPatternMap();

        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/patternfilledbandxrayscatter.bmp");

        patternMap.assertEquals(expected);
    }



    @Test
    public void testFilledBandXrayScatter() {
        PatternFilledBandXrayScatter tmpPattern =
                new PatternFilledBandXrayScatter();
        assertEquals(PatternFilledBandXrayScatter.DEFAULT_WIDTH,
                tmpPattern.width);
        assertEquals(PatternFilledBandXrayScatter.DEFAULT_HEIGHT,
                tmpPattern.height);
        assertEquals(PatternFilledBandXrayScatter.DEFAULT_MAX_INDEX,
                tmpPattern.maxIndex);
    }



    @Test
    public void testFilledBandXrayScatterIntInt() {
        assertEquals(336, patternOp.width);
        assertEquals(256, patternOp.height);
        assertEquals(5, patternOp.maxIndex);
    }



    @Test
    public void testToString() {
        assertEquals(patternOp.toString(),
                "PatternFilledBandXrayScatter [width=336, height=256, maxIndex=5]");
    }

}
