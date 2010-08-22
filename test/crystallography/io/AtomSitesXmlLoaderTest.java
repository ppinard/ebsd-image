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

import static crystallography.io.AtomSitesXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jdom.Element;
import org.jdom.IllegalNameException;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.AtomSite;
import crystallography.core.AtomSites;

public class AtomSitesXmlLoaderTest {

    private Element element;

    private AtomSite atom1;

    private AtomSite atom2;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        atom1 = new AtomSite(13, 0.1, 0.2, 0.3);
        element.addContent(new AtomSiteXmlSaver().save(atom1));
        atom2 = new AtomSite(14, 0.4, 0.5, 0.6);
        element.addContent(new AtomSiteXmlSaver().save(atom2));
    }



    @Test
    public void testLoad() {
        AtomSites atoms = new AtomSitesXmlLoader().load(element);

        assertEquals(2, atoms.size());
        assertTrue(atoms.contains(atom1));
        assertTrue(atoms.contains(atom2));
    }



    @Test(expected = IllegalNameException.class)
    public void testLoadException() {
        Element element = new Element("Incorrect");
        new AtomSitesXmlLoader().load(element);
    }

}
