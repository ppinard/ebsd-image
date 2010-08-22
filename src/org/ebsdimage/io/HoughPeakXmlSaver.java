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

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML saver for <code>HoughPeak</code>.
 * 
 * @author Philippe T. Pinard
 */
public class HoughPeakXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(HoughPeak)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((HoughPeak) obj);
    }



    /**
     * Saves a <code>HoughPeak</code> to an XML <code>Element</code>.
     * 
     * @param peak
     *            a <code>HoughPeak</code>
     * @return an XML <code>Element</code>
     */
    public Element save(HoughPeak peak) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_RHO, Double.toString(peak.rho));
        element.setAttribute(ATTR_THETA, Double.toString(peak.theta));
        element.setAttribute(ATTR_THETA_UNITS, UnitsXmlTags.RAD);
        element.setAttribute(ATTR_MAXINTENSITY, Double.toString(peak.intensity));

        return element;
    }

}
