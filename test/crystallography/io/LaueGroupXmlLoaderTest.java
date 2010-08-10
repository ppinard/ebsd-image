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

import static crystallography.io.LaueGroupXmlTags.ATTR_CRYSTAL_SYSTEM;
import static crystallography.io.LaueGroupXmlTags.ATTR_INDEX;
import static crystallography.io.LaueGroupXmlTags.ATTR_SYMBOL;
import static crystallography.io.LaueGroupXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.jdom.Element;
import org.jdom.IllegalNameException;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.CrystalSystem;
import crystallography.core.LaueGroup;

public class LaueGroupXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_INDEX, Integer.toString(1));
        element.setAttribute(ATTR_SYMBOL, "1");
        element.setAttribute(ATTR_CRYSTAL_SYSTEM,
                CrystalSystem.TRICLINIC.toString());
    }



    @Test
    public void testLoad() {
        LaueGroup pg = new LaueGroupXmlLoader().load(element);
        LaueGroup expected = LaueGroup.LG1;

        assertEquals(expected.index, pg.index);
        assertEquals(expected.symbol, pg.symbol);
        assertEquals(expected.crystalSystem, pg.crystalSystem);
    }



    @Test(expected = IllegalNameException.class)
    public void testLoadExceptionElementName() {
        new LaueGroupXmlLoader().load(new Element("Incorrect"));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testLoadExceptionHMSymbol() {
        element.setAttribute(ATTR_SYMBOL, "2");

        new LaueGroupXmlLoader().load(element);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testLoadExceptionSchoenfliesSymbol() {
        element.setAttribute(ATTR_CRYSTAL_SYSTEM,
                CrystalSystem.CUBIC.toString());

        new LaueGroupXmlLoader().load(element);
    }

}
