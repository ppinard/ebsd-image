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
import org.jdom.IllegalNameException;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.Plane;

public class PlaneXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_H, Integer.toString(1));
        element.setAttribute(ATTR_K, Integer.toString(2));
        element.setAttribute(ATTR_L, Integer.toString(3));
    }



    @Test
    public void testLoad() {
        Plane plane = new PlaneXmlLoader().load(element);

        assertEquals(1, (int) plane.get(0));
        assertEquals(2, (int) plane.get(1));
        assertEquals(3, (int) plane.get(2));
    }



    @Test(expected = IllegalNameException.class)
    public void testLoadException() {
        new PlaneXmlLoader().load(new Element("Incorrect"));
    }

}
