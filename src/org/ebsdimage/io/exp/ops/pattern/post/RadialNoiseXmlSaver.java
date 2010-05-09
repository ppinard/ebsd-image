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
package org.ebsdimage.io.exp.ops.pattern.post;

import static org.ebsdimage.io.exp.ops.pattern.post.RadialNoiseXmlTags.*;

import org.ebsdimage.core.exp.ops.pattern.post.RadialNoise;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML saver for a <code>RadialNoise</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class RadialNoiseXmlSaver implements ObjectXmlSaver {
    /**
     * {@inheritDoc}
     * 
     * @see #save(RadialNoise)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((RadialNoise) obj);
    }



    /**
     * Saves a <code>RadialNoise</code> operation to an XML <code>Element</code>
     * .
     * 
     * @param op
     *            a <code>RadialNoise</code> operation
     * @return an XML <code>Element</code>
     */
    public Element save(RadialNoise op) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_X, Integer.toString(op.x));
        element.setAttribute(ATTR_Y, Integer.toString(op.y));
        element.setAttribute(UnitsXmlTags.ATTR, UnitsXmlTags.PX);
        element.setAttribute(ATTR_STDDEVX, Double.toString(op.stdDevX));
        element.setAttribute(ATTR_STDDEVY, Double.toString(op.stdDevY));
        element.setAttribute(ATTR_INITIALNOISESTDDEV, Double
                .toString(op.initialNoiseStdDev));
        element.setAttribute(ATTR_FINALNOISESTDDEV, Double
                .toString(op.finalNoiseStdDev));

        return element;
    }

}
