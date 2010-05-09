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
import static ptpshared.io.math.EulersXmlTags.ATTR_THETA1;
import static ptpshared.io.math.EulersXmlTags.ATTR_THETA2;
import static ptpshared.io.math.EulersXmlTags.ATTR_THETA3;
import static ptpshared.io.math.EulersXmlTags.TAG_NAME;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Eulers;
import ptpshared.io.math.EulersXmlLoader;

public class EulersXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);
        element.setAttribute(ATTR_THETA1, Double.toString(0.1));
        element.setAttribute(ATTR_THETA2, Double.toString(0.2));
        element.setAttribute(ATTR_THETA3, Double.toString(0.3));
    }



    @Test
    public void testLoadElement() {
        Eulers eulers = new EulersXmlLoader().load(element);

        assertEquals(0.1, eulers.theta1, 1e-7);
        assertEquals(0.2, eulers.theta2, 1e-7);
        assertEquals(0.3, eulers.theta3, 1e-7);
    }

}
