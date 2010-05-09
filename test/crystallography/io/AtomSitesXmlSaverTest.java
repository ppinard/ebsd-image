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

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.AtomSite;
import crystallography.core.AtomSites;
import crystallography.io.AtomSitesXmlSaver;

import ptpshared.utility.xml.ObjectXml;

public class AtomSitesXmlSaverTest {

    private AtomSites atoms;



    @Before
    public void setUp() throws Exception {
        atoms = new AtomSites();

        AtomSite atom1 = new AtomSite(13, 0.1, 0.2, 0.3);
        atoms.add(atom1);
        AtomSite atom2 = new AtomSite(14, 0.4, 0.5, 0.6);
        atoms.add(atom2);
    }



    @Test
    public void testSaveAtomSites() {
        Element element = new AtomSitesXmlSaver().save(atoms);
        testElement(element);
    }



    @Test
    public void testSaveObjectXml() {
        Element element = new AtomSitesXmlSaver().save((ObjectXml) atoms);
        testElement(element);
    }



    private void testElement(Element element) {
        assertEquals(TAG_NAME, element.getName());
        assertEquals(2, element.getChildren().size());
        // Refer to AtomSiteXmlSaverTest for tests of the actual atom site.
    }

}
