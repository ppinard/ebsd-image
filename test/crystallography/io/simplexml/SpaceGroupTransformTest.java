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
package crystallography.io.simplexml;

import org.junit.Before;
import org.junit.Test;

import crystallography.core.SpaceGroup;
import crystallography.core.SpaceGroups;

import static org.junit.Assert.assertEquals;

public class SpaceGroupTransformTest {

    private SpaceGroupTransform transform;



    @Before
    public void setUp() throws Exception {
        transform = new SpaceGroupTransform();
    }



    @Test
    public void testRead() throws Exception {
        String value = "216";

        SpaceGroup expected = SpaceGroups.fromIndex(216);
        SpaceGroup actual = transform.read(value);

        assertEquals(expected, actual);
    }



    @Test
    public void testWrite() throws Exception {
        SpaceGroup m = SpaceGroups.fromIndex(216);

        String expected = "216";
        String actual = transform.write(m);

        assertEquals(expected, actual);
    }

}
