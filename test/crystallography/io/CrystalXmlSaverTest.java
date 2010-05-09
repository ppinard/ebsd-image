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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXml;
import crystallography.core.AtomSites;
import crystallography.core.Crystal;
import crystallography.core.UnitCell;
import crystallography.core.crystals.IronBCC;

public class CrystalXmlSaverTest {

    private Crystal crystal;



    @Before
    public void setUp() throws Exception {
        crystal = new IronBCC();
    }



    @Test
    public void testSaveCrystal() {
        Element element = new CrystalXmlSaver().save(crystal);
        testElement(element);
    }



    @Test
    public void testSaveObjectXml() {
        Element element = new CrystalXmlSaver().save((ObjectXml) crystal);
        testElement(element);
    }



    private void testElement(Element element) {
        assertEquals(IronBCC.class.getSimpleName(), element.getName());
        assertEquals(crystal.name, JDomUtil.getStringFromAttribute(element,
                ATTR_NAME));
        UnitCell unitCell = new UnitCellXmlLoader().load(JDomUtil.getChild(
                element, UnitCellXmlTags.TAG_NAME));
        assertTrue(crystal.unitCell.equals(unitCell, 1e-7));
        AtomSites atoms = new AtomSitesXmlLoader().load(JDomUtil.getChild(
                element, AtomSitesXmlTags.TAG_NAME));
        assertTrue(crystal.atoms.equals(atoms, 1e-7));
    }

}
