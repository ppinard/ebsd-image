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
package org.ebsdimage.io;

import static org.ebsdimage.io.HoughPeakXmlTags.*;

import org.ebsdimage.core.HoughPeak;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.BadUnitException;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML loader for <code>HoughPeak</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class HoughPeakXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>HoughPeak</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>HoughPeak</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     * @throws BadUnitException
     *             if the units for theta are not radians
     */
    @Override
    public HoughPeak load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");
        if (!JDomUtil.getStringFromAttributeDefault(element, ATTR_THETA_UNITS,
                UnitsXmlTags.RAD).equals(UnitsXmlTags.RAD))
            throw new BadUnitException("Units for theta must be rad.");

        double rho = JDomUtil.getDoubleFromAttribute(element, ATTR_RHO);
        double theta = JDomUtil.getDoubleFromAttribute(element, ATTR_THETA);

        HoughPeak peak;
        if (JDomUtil.hasAttribute(element, ATTR_MAXINTENSITY)) {
            double maxIntensity =
                    JDomUtil.getDoubleFromAttribute(element, ATTR_MAXINTENSITY);
            peak = new HoughPeak(rho, theta, maxIntensity);
        } else {
            peak = new HoughPeak(rho, theta);
        }

        return peak;
    }

}
