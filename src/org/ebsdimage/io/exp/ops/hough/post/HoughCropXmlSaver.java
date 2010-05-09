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
package org.ebsdimage.io.exp.ops.hough.post;

import static org.ebsdimage.io.exp.ops.hough.post.HoughCropXmlTags.ATTR_RADIUS;
import static org.ebsdimage.io.exp.ops.hough.post.HoughCropXmlTags.TAG_NAME;

import org.ebsdimage.core.exp.ops.hough.post.HoughCrop;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML saver for a <code>HoughCrop</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class HoughCropXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(HoughCrop)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((HoughCrop) obj);
    }



    /**
     * Saves a <code>HoughCrop</code> operation to an XML <code>Element</code>.
     * 
     * @param op
     *            a <code>HoughCrop</code> operation
     * @return an XML <code>Element</code>
     */
    public Element save(HoughCrop op) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_RADIUS, Integer.toString(op.radius));
        element.setAttribute(UnitsXmlTags.ATTR, UnitsXmlTags.PX);

        return element;
    }

}
