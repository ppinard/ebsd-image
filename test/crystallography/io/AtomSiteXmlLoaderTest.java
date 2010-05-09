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
import org.jdom.IllegalNameException;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Vector3D;
import ptpshared.io.math.Vector3DXmlSaver;
import crystallography.core.AtomSite;

public class AtomSiteXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        Vector3D position = new Vector3D(0.1, 0.2, 0.3);
        element.addContent(new Vector3DXmlSaver().save(position).setName(
                CHILD_POSITION));

        element.addContent(new Element(CHILD_ELEMENT).setAttribute(
                ATTR_ATOMICNUMBER, Integer.toString(13)));
    }



    @Test
    public void testLoad() {
        AtomSite atom = new AtomSiteXmlLoader().load(element);

        assertEquals(13, atom.atomicNumber);
        assertEquals(0.1, atom.position.get(0), 1e-7);
        assertEquals(0.2, atom.position.get(1), 1e-7);
        assertEquals(0.3, atom.position.get(2), 1e-7);
    }



    @Test(expected = IllegalNameException.class)
    public void testLoadException() {
        Element element = new Element("Incorrect");
        new AtomSiteXmlLoader().load(element);
    }

}
