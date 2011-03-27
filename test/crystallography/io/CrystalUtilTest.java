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
package crystallography.io;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class CrystalUtilTest extends TestCase {

    private File dir;



    @Before
    public void setUp() throws Exception {
        dir = getFile("crystallography/testdata/");
    }



    @Test
    public void testListCrystals() throws IOException {
        Crystal[] crystals = CrystalUtil.listCrystals(dir);

        assertEquals(1, crystals.length);
        assertEquals(CrystalFactory.silicon(), crystals[0], 1e-6);
    }



    @Test
    public void testListCrystals2() throws IOException {
        Crystal[] crystals =
                CrystalUtil.listCrystals(getFile("crystallography/io/"));

        assertEquals(0, crystals.length);
    }

}
