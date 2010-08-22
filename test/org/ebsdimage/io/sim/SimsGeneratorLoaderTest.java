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
package org.ebsdimage.io.sim;

import java.io.File;

import org.ebsdimage.core.sim.SimsGeneratorTester;
import org.junit.Before;

import rmlshared.io.FileUtil;

public class SimsGeneratorLoaderTest extends SimsGeneratorTester {

    private static File file =
            FileUtil.getFile("org/ebsdimage/testdata/sims_generator.xml");



    @Before
    public void setUp() throws Exception {
        generator = new SimsGeneratorLoader().load(file);
    }

}
