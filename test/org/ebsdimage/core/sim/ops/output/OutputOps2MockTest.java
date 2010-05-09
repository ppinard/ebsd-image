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
package org.ebsdimage.core.sim.ops.output;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.ebsdimage.core.sim.ops.output.OutputOps;
import org.junit.Before;
import org.junit.Test;

public class OutputOps2MockTest {

    private OutputOps op;



    @Before
    public void setUp() throws Exception {
        op = new OutputOps2Mock();
    }



    @Test
    public void testSave() throws IOException {
        op.save(null);
        assertTrue(true);
    }

}
