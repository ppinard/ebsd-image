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
package ptpshared.util.simplexml;

import static net.sf.magnitude.test.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import net.sf.magnitude.core.Magnitude;

import org.junit.Before;
import org.junit.Test;

public class MagnitudeTransformTest {

    private MagnitudeTransform transform;



    @Before
    public void setUp() throws Exception {
        transform = new MagnitudeTransform();
    }



    @Test
    public void testRead() throws Exception {
        String value = "1.23 m.s-1";

        Magnitude expected = new Magnitude(1.23, "m.s-1");
        Magnitude actual = transform.read(value);

        assertEquals(expected, actual, new Magnitude(1e-3, "m.s-1"));
    }



    @Test
    public void testWrite() throws Exception {
        Magnitude m = new Magnitude(1.23, "m.s-1");

        String expected = "1.23 m.s-1";
        String actual = transform.write(m);

        assertEquals(expected, actual);
    }

}
