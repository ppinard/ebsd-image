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

import static crystallography.io.AtomSiteXmlTags.ATTR_ATOMICNUMBER;
import static crystallography.io.AtomSiteXmlTags.CHILD_ELEMENT;
import static crystallography.io.AtomSiteXmlTags.CHILD_POSITION;
import static crystallography.io.AtomSiteXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Vector3D;
import ptpshared.io.math.Vector3DXmlLoader;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXml;
import crystallography.core.AtomSite;

public class AtomSiteXmlSaverTest {

    private AtomSite atom;



    @Before
    public void setUp() throws Exception {
        atom = new AtomSite(13, 0.1, 0.2, 0.3);
    }



    @Test
    public void testSaveAtomSite() {
        Element element = new AtomSiteXmlSaver().save(atom);
        testElement(element);
    }



    @Test
    public void testSaveObjectXml() {
        Element element = new AtomSiteXmlSaver().save((ObjectXml) atom);
        testElement(element);
    }



    private void testElement(Element element) {
        assertEquals(TAG_NAME, element.getName());
        assertEquals(13, JDomUtil.getIntegerFromAttribute(element,
                CHILD_ELEMENT, ATTR_ATOMICNUMBER));

        Vector3D position = new Vector3DXmlLoader().load(JDomUtil.getChild(
                element, CHILD_POSITION), CHILD_POSITION);
        assertEquals(0.1, position.get(0), 1e-7);
        assertEquals(0.2, position.get(1), 1e-7);
        assertEquals(0.3, position.get(2), 1e-7);
    }

}
