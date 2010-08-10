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
import org.jdom.IllegalNameException;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.*;
import crystallography.core.crystals.Silicon;

public class CrystalXmlLoaderTest {

    private Element element;
    private UnitCell unitCell;
    private AtomSites atoms;
    private LaueGroup pointGroup;



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

        pointGroup = LaueGroup.LG1;
        element.addContent(new LaueGroupXmlSaver().save(pointGroup));
    }



    @Test
    public void testLoad() {
        Crystal crystal = new CrystalXmlLoader().load(element);

        assertEquals("crystal1", crystal.name);
        assertTrue(unitCell.equals(crystal.unitCell, 1e-7));
        assertTrue(atoms.equals(crystal.atoms, 1e-7));
        assertTrue(pointGroup.equals(crystal.laueGroup));
    }



    @Test
    public void testLoadSpecificCrystal() {
        Element element = new Element(Silicon.class.getSimpleName());

        Crystal crystal = new CrystalXmlLoader().load(element);

        assertEquals("Silicon", crystal.name);
        assertTrue(UnitCellFactory.cubic(5.43).equals(crystal.unitCell, 1e-7));
        assertTrue(AtomSitesFactory.atomSitesFCC(14)
                .equals(crystal.atoms, 1e-7));
        assertEquals(LaueGroup.LGm3m, crystal.laueGroup);
    }



    @Test(expected = IllegalNameException.class)
    public void testLoadSpecificCrystalException() {
        Element element = new Element("Incorrect");
        new CrystalXmlLoader().load(element);
    }



    @Test
    public void testLoadSpecificCrystalWithModAtomSites() {
        Element element = new Element(Silicon.class.getSimpleName());
        element.setAttribute(ATTR_NAME, "SiliconMod");
        element.addContent(new AtomSitesXmlSaver().save(atoms));

        Crystal crystal = new CrystalXmlLoader().load(element);

        assertEquals("SiliconMod", crystal.name);
        assertTrue(new Silicon().unitCell.equals(crystal.unitCell, 1e-7));
        assertTrue(atoms.equals(crystal.atoms, 1e-7));
        assertEquals(new Silicon().laueGroup, crystal.laueGroup);
    }



    @Test
    public void testLoadSpecificCrystalWithModUnitCell() {
        Element element = new Element(Silicon.class.getSimpleName());
        element.setAttribute(ATTR_NAME, "SiliconMod");
        element.addContent(new UnitCellXmlSaver().save(unitCell));
        element.addContent(new LaueGroupXmlSaver().save(LaueGroup.LG1));

        Crystal crystal = new CrystalXmlLoader().load(element);

        assertEquals("SiliconMod", crystal.name);
        assertTrue(unitCell.equals(crystal.unitCell, 1e-7));
        assertTrue(new Silicon().atoms.equals(crystal.atoms, 1e-7));
        assertEquals(LaueGroup.LG1, crystal.laueGroup);
    }



    @Test
    public void testLoadSpecificCrystalWithModPointGroup() {
        Element element = new Element(Silicon.class.getSimpleName());
        element.setAttribute(ATTR_NAME, "SiliconMod");
        LaueGroup pg = LaueGroup.LGm3;
        element.addContent(new LaueGroupXmlSaver().save(pg));

        Crystal crystal = new CrystalXmlLoader().load(element);

        assertEquals("SiliconMod", crystal.name);
        assertTrue(new Silicon().unitCell.equals(crystal.unitCell, 1e-7));
        assertTrue(new Silicon().atoms.equals(crystal.atoms, 1e-7));
        assertEquals(pg, crystal.laueGroup);
    }
}
