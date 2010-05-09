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
package ptpshared.io.math;

import static ptpshared.io.math.EulersXmlTags.ATTR_THETA1;
import static ptpshared.io.math.EulersXmlTags.ATTR_THETA2;
import static ptpshared.io.math.EulersXmlTags.ATTR_THETA3;
import static ptpshared.io.math.EulersXmlTags.TAG_NAME;
import static ptpshared.utility.xml.UnitsXmlTags.RAD;

import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.core.math.Eulers;
import ptpshared.utility.BadUnitException;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML loader for <code>Eulers</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class EulersXmlLoader implements ObjectXmlLoader {

    /**
     * Loads an <code>Eulers</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return an <code>Eulers</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     * @throws BadUnitException
     *             if the unit of the angle are not radians
     */
    @Override
    public Eulers load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");
        if (!JDomUtil.getStringFromAttributeDefault(element, UnitsXmlTags.ATTR,
                UnitsXmlTags.RAD).equals(UnitsXmlTags.RAD))
            throw new BadUnitException("Unit for euler angles should be " + RAD);

        return new Eulers(
                JDomUtil.getDoubleFromAttribute(element, ATTR_THETA1), JDomUtil
                        .getDoubleFromAttribute(element, ATTR_THETA2), JDomUtil
                        .getDoubleFromAttribute(element, ATTR_THETA3));
    }

}
