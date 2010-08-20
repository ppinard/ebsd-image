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

import static crystallography.io.SpaceGroupXmlTags.ATTR_INDEX;
import static crystallography.io.SpaceGroupXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXml;
import crystallography.core.SpaceGroup;
import crystallography.core.SpaceGroups;

public class SpaceGroupXmlSaverTest {

    private SpaceGroup sg;



    @Before
    public void setUp() throws Exception {
        sg = SpaceGroups.SG1;
    }



    @Test
    public void testSaveObjectXml() {
        Element element = new SpaceGroupXmlSaver().save((ObjectXml) sg);
        testElement(element);
    }



    @Test
    public void testSavePointGroup() {
        Element element = new SpaceGroupXmlSaver().save(sg);
        testElement(element);
    }



    private void testElement(Element element) {
        assertEquals(TAG_NAME, element.getName());
        assertEquals(1, JDomUtil.getIntegerFromAttribute(element, ATTR_INDEX));
    }
}
