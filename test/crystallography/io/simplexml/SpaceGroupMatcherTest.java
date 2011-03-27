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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SpaceGroupMatcherTest {
    private SpaceGroupMatcher matcher;



    @Before
    public void setUp() throws Exception {
        matcher = new SpaceGroupMatcher();
    }



    @Test
    public void testMatch() throws Exception {
        assertNotNull(matcher.match(SpaceGroup.class));
        assertNull(matcher.match(new Object().getClass()));
    }
}
