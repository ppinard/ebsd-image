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

import static crystallography.io.PointGroupXmlTags.*;
import static org.junit.Assert.assertEquals;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXml;
import crystallography.core.CrystalSystem;
import crystallography.core.PointGroup;

public class PointGroupXmlSaverTest {

    private PointGroup pg;



    @Before
    public void setUp() throws Exception {
        pg = PointGroup.PG1;
    }



    @Test
    public void testSaveObjectXml() {
        Element element = new PointGroupXmlSaver().save((ObjectXml) pg);
        testElement(element);
    }



    @Test
    public void testSavePointGroup() {
        Element element = new PointGroupXmlSaver().save(pg);
        testElement(element);
    }



    private void testElement(Element element) {
        assertEquals(TAG_NAME, element.getName());
        assertEquals(1, JDomUtil.getIntegerFromAttribute(element,
                ATTR_LAUE_GROUP));
        assertEquals(pg.hmSymbol, JDomUtil.getStringFromAttribute(element,
                ATTR_HM));
        assertEquals(pg.schoenfliesSymbol, JDomUtil.getStringFromAttribute(
                element, ATTR_SCHOENFLIES));
        assertEquals(pg.crystalSystem, CrystalSystem.valueOf(JDomUtil
                .getStringFromAttribute(element, ATTR_CRYSTAL_SYSTEM)));
    }
}
