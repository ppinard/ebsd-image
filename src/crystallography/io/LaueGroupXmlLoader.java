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

import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import crystallography.core.CrystalSystem;
import crystallography.core.LaueGroup;

/**
 * XML loader for <code>PointGroup</code>.
 * 
 * @author Philippe T. Pinard
 */
public class LaueGroupXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>LaueGroup</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>LaueGroup</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public LaueGroup load(Element element) {
        // Fix for old Laue Group XML Element
        if (element.getName().equals("PointGroup"))
            return loadOld(element);

        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        int index = JDomUtil.getIntegerFromAttribute(element, ATTR_INDEX);
        LaueGroup pg = LaueGroup.fromIndex(index);

        // Verify
        String actual;
        String expected;

        // Symbol
        actual = JDomUtil.getStringFromAttribute(element, ATTR_SYMBOL);
        expected = pg.symbol;
        if (!actual.equals(expected))
            throw new IllegalArgumentException("Schoenflies symbol should be "
                    + expected + ", not " + actual + ".");

        // Crystal system
        CrystalSystem actualSystem =
                CrystalSystem.valueOf(JDomUtil.getStringFromAttribute(element,
                        ATTR_CRYSTAL_SYSTEM));
        CrystalSystem expectedSystem = pg.crystalSystem;
        if (!actualSystem.equals(expectedSystem))
            throw new IllegalArgumentException("Crystal system should be "
                    + expectedSystem + ", not " + expectedSystem + ".");

        return pg;
    }



    /**
     * Old loader for XML element using the Point Group class instead of the new
     * Laue Group class.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a new <code>LaueGroup</code>
     */
    private LaueGroup loadOld(Element element) {
        int index = JDomUtil.getIntegerFromAttribute(element, "laueGroup");
        LaueGroup pg = LaueGroup.fromIndex(index);

        // Verify crystal system
        CrystalSystem actualSystem =
                CrystalSystem.valueOf(JDomUtil.getStringFromAttribute(element,
                        ATTR_CRYSTAL_SYSTEM));
        CrystalSystem expectedSystem = pg.crystalSystem;
        if (!actualSystem.equals(expectedSystem))
            throw new IllegalArgumentException("Crystal system should be "
                    + expectedSystem + ", not " + actualSystem + ".");

        return pg;
    }
}
