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
import org.jdom.IllegalNameException;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.CrystalSystem;
import crystallography.core.PointGroup;

public class PointGroupXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_LAUE_GROUP, Integer.toString(1));
        element.setAttribute(ATTR_HM, "1");
        element.setAttribute(ATTR_SCHOENFLIES, "C1");
        element.setAttribute(ATTR_CRYSTAL_SYSTEM, CrystalSystem.TRICLINIC
                .toString());
    }



    @Test
    public void testLoad() {
        PointGroup pg = new PointGroupXmlLoader().load(element);
        PointGroup expected = PointGroup.PG1;

        assertEquals(expected.laueGroup, pg.laueGroup);
        assertEquals(expected.hmSymbol, pg.hmSymbol);
        assertEquals(expected.schoenfliesSymbol, pg.schoenfliesSymbol);
        assertEquals(expected.crystalSystem, pg.crystalSystem);
    }



    @Test(expected = IllegalNameException.class)
    public void testLoadExceptionElementName() {
        new PointGroupXmlLoader().load(new Element("Incorrect"));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testLoadExceptionHMSymbol() {
        element.setAttribute(ATTR_HM, "2");

        new PointGroupXmlLoader().load(element);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testLoadExceptionSchoenfliesSymbol() {
        element.setAttribute(ATTR_SCHOENFLIES, "C2");

        new PointGroupXmlLoader().load(element);
    }

}
