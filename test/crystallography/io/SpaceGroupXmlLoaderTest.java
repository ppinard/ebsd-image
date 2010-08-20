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
import org.jdom.IllegalNameException;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.SpaceGroup;
import crystallography.core.SpaceGroups;

public class SpaceGroupXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_INDEX, Integer.toString(1));
    }



    @Test
    public void testLoad() {
        SpaceGroup sg = new SpaceGroupXmlLoader().load(element);
        SpaceGroup expected = SpaceGroups.SG1;

        assertEquals(expected.index, sg.index);
        assertEquals(expected.symbol, sg.symbol);
        assertEquals(expected.crystalSystem, sg.crystalSystem);
    }



    @Test(expected = IllegalNameException.class)
    public void testLoadExceptionElementName() {
        new SpaceGroupXmlLoader().load(new Element("Incorrect"));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testLoadExceptionIndex() {
        element.setAttribute(ATTR_INDEX, "231");

        new SpaceGroupXmlLoader().load(element);
    }

}
