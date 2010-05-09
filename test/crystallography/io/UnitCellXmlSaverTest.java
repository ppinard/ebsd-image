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

import static crystallography.io.UnitCellXmlTags.*;
import static org.junit.Assert.assertEquals;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.UnitCell;
import crystallography.io.UnitCellXmlSaver;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.UnitsXmlTags;

public class UnitCellXmlSaverTest {

    private UnitCell unitCell;



    @Before
    public void setUp() throws Exception {
        unitCell = new UnitCell(1.0, 2.0, 3.0, 1.1, 2.1, 3.1);
    }



    @Test
    public void testSaveUnitCell() {
        Element element = new UnitCellXmlSaver().save(unitCell);
        testElement(element);
    }



    @Test
    public void testSaveObjectXml() {
        Element element = new UnitCellXmlSaver().save((ObjectXml) unitCell);
        testElement(element);
    }



    private void testElement(Element element) {
        assertEquals(TAG_NAME, element.getName());
        assertEquals(1.0, JDomUtil.getDoubleFromAttribute(element, ATTR_A),
                1e-7);
        assertEquals(2.0, JDomUtil.getDoubleFromAttribute(element, ATTR_B),
                1e-7);
        assertEquals(3.0, JDomUtil.getDoubleFromAttribute(element, ATTR_C),
                1e-7);
        assertEquals(UnitsXmlTags.ANGSTROM, JDomUtil.getStringFromAttribute(
                element, ATTR_DIM_UNITS));
        assertEquals(1.1, JDomUtil.getDoubleFromAttribute(element, ATTR_ALPHA),
                1e-7);
        assertEquals(2.1, JDomUtil.getDoubleFromAttribute(element, ATTR_BETA),
                1e-7);
        assertEquals(3.1, JDomUtil.getDoubleFromAttribute(element, ATTR_GAMMA),
                1e-7);
        assertEquals(UnitsXmlTags.RAD, JDomUtil.getStringFromAttribute(element,
                ATTR_ANGLE_UNITS));
    }
}
