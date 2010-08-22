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
import static ptpshared.io.math.Vector3DXmlTags.ATTR_X;
import static ptpshared.io.math.Vector3DXmlTags.ATTR_Y;
import static ptpshared.io.math.Vector3DXmlTags.ATTR_Z;
import static ptpshared.io.math.Vector3DXmlTags.TAG_NAME;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Vector3D;
import ptpshared.utility.xml.JDomUtil;

public class Vector3DXmlSaverTest {

    private Vector3D vector;



    @Before
    public void setUp() throws Exception {
        vector = new Vector3D(1.0, 2.0, 3.0);
    }



    @Test
    public void testSaveVector3D() {
        Element element = new Vector3DXmlSaver().save(vector);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(1.0, JDomUtil.getDoubleFromAttribute(element, ATTR_X),
                1e-7);
        assertEquals(2.0, JDomUtil.getDoubleFromAttribute(element, ATTR_Y),
                1e-7);
        assertEquals(3.0, JDomUtil.getDoubleFromAttribute(element, ATTR_Z),
                1e-7);
    }

}
