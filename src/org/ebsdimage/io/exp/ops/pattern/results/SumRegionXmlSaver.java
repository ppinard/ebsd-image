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
package org.ebsdimage.io.exp.ops.pattern.results;

import static org.ebsdimage.io.exp.ops.pattern.results.SumRegionXmlTags.*;

import org.ebsdimage.core.exp.ops.pattern.results.SumRegion;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;

/**
 * XML saver for an <code>SumRegion</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class SumRegionXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(SumRegion)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((SumRegion) obj);
    }



    /**
     * Saves an <code>SumRegion</code> operation to an XML <code>Element</code>.
     * 
     * @param op
     *            an <code>SumRegion</code>
     * @return an XML <code>Element</code>
     */
    public Element save(SumRegion op) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_XMIN, Double.toString(op.xmin));
        element.setAttribute(ATTR_YMIN, Double.toString(op.ymin));
        element.setAttribute(ATTR_XMAX, Double.toString(op.xmax));
        element.setAttribute(ATTR_YMAX, Double.toString(op.ymax));

        return element;
    }

}
