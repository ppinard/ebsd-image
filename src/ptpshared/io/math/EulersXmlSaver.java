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

import org.jdom.Element;

import ptpshared.core.math.Eulers;
import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML saver for <code>AtomSite</code>.
 * 
 * @author Philippe T. Pinard
 */
public class EulersXmlSaver implements ObjectXmlSaver {

    /**
     * Saves the <code>Eulers</code> to an XML <code>Element</code>.
     * 
     * @param eulers
     *            an <code>Eulers</code>
     * @return an XML <code>Element</code>
     */
    public Element save(Eulers eulers) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_THETA1, Double.toString(eulers.theta1));
        element.setAttribute(ATTR_THETA2, Double.toString(eulers.theta2));
        element.setAttribute(ATTR_THETA3, Double.toString(eulers.theta3));
        element.setAttribute(UnitsXmlTags.ATTR, UnitsXmlTags.RAD);

        return element;
    }



    /**
     * {@inheritDoc}
     * 
     * @see #save(Eulers)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((Eulers) obj);
    }

}
