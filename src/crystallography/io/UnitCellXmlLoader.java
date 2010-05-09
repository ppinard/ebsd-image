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

import static crystallography.io.UnitCellXmlTags.*;
import static ptpshared.utility.xml.UnitsXmlTags.ANGSTROM;
import static ptpshared.utility.xml.UnitsXmlTags.RAD;

import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.BadUnitException;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import crystallography.core.UnitCell;

/**
 * XML loader for <code>UnitCell</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class UnitCellXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>UnitCell</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>UnitCell</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     * @throws BadUnitException
     *             if a unit in the <code>Element</code> is incorrect.
     */
    @Override
    public UnitCell load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");
        if (!(JDomUtil.getStringFromAttributeDefault(element, ATTR_DIM_UNITS,
                ANGSTROM).equals(ANGSTROM)))
            throw new BadUnitException(
                    "Unit for unit cell dimensions should be " + ANGSTROM);
        if (!(JDomUtil.getStringFromAttributeDefault(element, ATTR_ANGLE_UNITS,
                RAD).equals(RAD)))
            throw new BadUnitException("Unit for unit cell angles should be "
                    + RAD);

        return new UnitCell(JDomUtil.getDoubleFromAttribute(element, ATTR_A),
                JDomUtil.getDoubleFromAttribute(element, ATTR_B), JDomUtil
                        .getDoubleFromAttribute(element, ATTR_C), JDomUtil
                        .getDoubleFromAttribute(element, ATTR_ALPHA), JDomUtil
                        .getDoubleFromAttribute(element, ATTR_BETA), JDomUtil
                        .getDoubleFromAttribute(element, ATTR_GAMMA));
    }
}
