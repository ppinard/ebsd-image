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
import ptpshared.utility.xml.JDomUtil;

public class QuaternionXmlSaverTest {

    private Quaternion q;



    @Before
    public void setUp() throws Exception {
        q = new Quaternion(1.0, -2.0, 3.0, 4.0);
    }



    @Test
    public void testSaveQuaternion() {
        Element element = new QuaternionXmlSaver().save(q);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(1.0, JDomUtil.getDoubleFromAttribute(element, ATTR_Q0),
                1e-7);
        assertEquals(-2.0, JDomUtil.getDoubleFromAttribute(element, ATTR_Q1),
                1e-7);
        assertEquals(3.0, JDomUtil.getDoubleFromAttribute(element, ATTR_Q2),
                1e-7);
        assertEquals(4.0, JDomUtil.getDoubleFromAttribute(element, ATTR_Q3),
                1e-7);
    }

}
