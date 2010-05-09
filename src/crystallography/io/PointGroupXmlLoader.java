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

import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import crystallography.core.CrystalSystem;
import crystallography.core.PointGroup;

/**
 * XML loader for <code>PointGroup</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PointGroupXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>PointGroup</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>PointGroup</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public PointGroup load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        int laueGroup =
                JDomUtil.getIntegerFromAttribute(element, ATTR_LAUE_GROUP);
        PointGroup pg = PointGroup.fromLaueGroup(laueGroup);

        // Verify
        String actual;
        String expected;

        // Schoenflies symbol
        actual = JDomUtil.getStringFromAttribute(element, ATTR_SCHOENFLIES);
        expected = pg.schoenfliesSymbol;
        if (!actual.equals(expected))
            throw new IllegalArgumentException("Schoenflies symbol should be "
                    + expected + ", not " + actual + ".");

        // Hermann-Mauguin symbol
        actual = JDomUtil.getStringFromAttribute(element, ATTR_HM);
        expected = pg.hmSymbol;
        if (!actual.equals(expected))
            throw new IllegalArgumentException(
                    "Herman-Mauguin symbol should be " + expected + ", not "
                            + actual + ".");

        // Crystal system
        CrystalSystem actualSystem =
                CrystalSystem.valueOf(JDomUtil.getStringFromAttribute(element,
                        ATTR_CRYSTAL_SYSTEM));
        CrystalSystem expectedSystem = pg.crystalSystem;
        if (!actualSystem.equals(expectedSystem))
            throw new IllegalArgumentException("Crystal system should be "
                    + expected + ", not " + actual + ".");

        return pg;
    }
}
