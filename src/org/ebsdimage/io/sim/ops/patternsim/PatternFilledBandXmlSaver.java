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
package org.ebsdimage.io.sim.ops.patternsim;

import static org.ebsdimage.io.sim.ops.patternsim.PatternFilledBandXmlTags.TAG_NAME;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_HEIGHT;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_MAXINDEX;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_SCATTER_TYPE;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_WIDTH;

import org.ebsdimage.core.sim.ops.patternsim.PatternFilledBand;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML saver for a <code>PatternFilledBand</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class PatternFilledBandXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(PatternFilledBand)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((PatternFilledBand) obj);
    }



    /**
     * Saves a <code>PatternFilledBand</code> operation to an XML
     * <code>Element</code>.
     * 
     * @param op
     *            a <code>PatternFilledBand</code> operation
     * @return an XML <code>Element</code>
     */
    public Element save(PatternFilledBand op) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_WIDTH, Integer.toString(op.width));
        element.setAttribute(ATTR_HEIGHT, Integer.toString(op.height));
        element.setAttribute(UnitsXmlTags.ATTR, UnitsXmlTags.PX);
        element.setAttribute(ATTR_MAXINDEX, Integer.toString(op.maxIndex));
        element.setAttribute(ATTR_SCATTER_TYPE, op.scatterType.toString());

        return element;
    }
}