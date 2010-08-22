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

import static crystallography.io.CrystalXmlTags.ATTR_NAME;
import static crystallography.io.CrystalXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.*;

public class CrystalXmlLoaderTest {

    private Element element;

    private UnitCell unitCell;

    private AtomSites atoms;

    private SpaceGroup sg;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_NAME, "crystal1");

        unitCell = new UnitCell(1.0, 2.0, 3.0, 1.1, 2.1, 3.1);
        element.addContent(new UnitCellXmlSaver().save(unitCell));

        atoms = new AtomSites();
        atoms.add(new AtomSite(13, 0.1, 0.2, 0.3));
        atoms.add(new AtomSite(14, 0.4, 0.5, 0.6));
        element.addContent(new AtomSitesXmlSaver().save(atoms));

        sg = SpaceGroups1.SG1;
        element.addContent(new SpaceGroupXmlSaver().save(sg));
    }



    @Test
    public void testLoad() {
        Crystal crystal = new CrystalXmlLoader().load(element);

        assertEquals("crystal1", crystal.name);
        assertTrue(unitCell.equals(crystal.unitCell, 1e-7));
        assertTrue(atoms.equals(crystal.atoms, 1e-7));
        assertTrue(sg.equals(crystal.spaceGroup));
    }

}
