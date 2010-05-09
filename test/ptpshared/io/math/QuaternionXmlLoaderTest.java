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
package ptpshared.io.math;

import static org.junit.Assert.assertEquals;
import static ptpshared.io.math.QuaternionXmlTags.*;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;

public class QuaternionXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);
        element.setAttribute(ATTR_Q0, Double.toString(1.0));
        element.setAttribute(ATTR_Q1, Double.toString(-2.0));
        element.setAttribute(ATTR_Q2, Double.toString(3.0));
        element.setAttribute(ATTR_Q3, Double.toString(4.0));
    }



    @Test
    public void testLoadElement() {
        Quaternion q = new QuaternionXmlLoader().load(element);

        assertEquals(1.0, q.getQ0(), 1e-7);
        assertEquals(-2.0, q.getQ1(), 1e-7);
        assertEquals(3.0, q.getQ2(), 1e-7);
        assertEquals(4.0, q.getQ3(), 1e-7);
    }

}
