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
package crystallography.io;

import static crystallography.io.PlaneXmlTags.ATTR_H;
import static crystallography.io.PlaneXmlTags.ATTR_K;
import static crystallography.io.PlaneXmlTags.ATTR_L;
import static crystallography.io.PlaneXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXml;
import crystallography.core.Plane;

public class PlaneXmlSaverTest {

    private Plane plane;



    @Before
    public void setUp() throws Exception {
        plane = new Plane(1, 2, 3);
    }



    @Test
    public void testSavePlane() {
        Element element = new PlaneXmlSaver().save(plane);
        testElement(element);
    }



    @Test
    public void testSaveObjectXml() {
        Element element = new PlaneXmlSaver().save((ObjectXml) plane);
        testElement(element);
    }



    private void testElement(Element element) {
        assertEquals(TAG_NAME, element.getName());
        assertEquals(1, JDomUtil.getIntegerFromAttribute(element, ATTR_H));
        assertEquals(2, JDomUtil.getIntegerFromAttribute(element, ATTR_K));
        assertEquals(3, JDomUtil.getIntegerFromAttribute(element, ATTR_L));
    }

}
