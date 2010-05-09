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
package org.ebsdimage.io.sim.ops.patternsim.op;

import static org.ebsdimage.io.sim.PatternXmlTags.ATTR_HEIGHT;
import static org.ebsdimage.io.sim.PatternXmlTags.ATTR_WIDTH;
import static org.ebsdimage.io.sim.ops.patternsim.op.PatternFilledBandXrayScatterXmlTags.ATTR_MAX_INDEX;
import static org.ebsdimage.io.sim.ops.patternsim.op.PatternFilledBandXrayScatterXmlTags.TAG_NAME;

import org.ebsdimage.core.sim.ops.patternsim.op.PatternFilledBandXrayScatter;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML saver for a <code>PatternFilledBandXrayScatter</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PatternFilledBandXrayScatterXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(PatternFilledBandXrayScatter)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((PatternFilledBandXrayScatter) obj);
    }



    /**
     * Saves a <code>PatternFilledBandXrayScatter</code> operation to an XML
     * <code>Element</code>.
     * 
     * @param op
     *            a <code>PatternFilledBandXrayScatter</code> operation
     * @return an XML <code>Element</code>
     */
    public Element save(PatternFilledBandXrayScatter op) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_WIDTH, Integer.toString(op.width));
        element.setAttribute(ATTR_HEIGHT, Integer.toString(op.height));
        element.setAttribute(UnitsXmlTags.ATTR, UnitsXmlTags.PX);
        element.setAttribute(ATTR_MAX_INDEX, Integer.toString(op.maxIndex));

        return element;
    }
}
