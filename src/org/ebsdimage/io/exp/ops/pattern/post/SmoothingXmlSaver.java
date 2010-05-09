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

import static org.ebsdimage.io.exp.ops.pattern.post.SmoothingXmlTags.ATTR_KERNELSIZE;
import static org.ebsdimage.io.exp.ops.pattern.post.SmoothingXmlTags.TAG_NAME;

import org.ebsdimage.core.exp.ops.pattern.post.Smoothing;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;

/**
 * XML saver for a <code>Smoothing</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class SmoothingXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(Smoothing)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((Smoothing) obj);
    }



    /**
     * Saves a <code>Smoothing</code> operation to an XML <code>Element</code>.
     * 
     * @param op
     *            a <code>Smoothing</code> operation
     * @return an XML <code>Element</code>
     */
    public Element save(Smoothing op) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_KERNELSIZE, Integer.toString(op.kernelSize));

        return element;
    }
}
