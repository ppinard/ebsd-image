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
package org.ebsdimage.io.exp.ops.indexing.op;

import static org.ebsdimage.io.exp.ops.indexing.op.KriegerLassen1994XmlTags.ATTR_MAX_INDEX;
import static org.ebsdimage.io.exp.ops.indexing.op.KriegerLassen1994XmlTags.ATTR_SCATTER_TYPE;
import static org.ebsdimage.io.exp.ops.indexing.op.KriegerLassen1994XmlTags.TAG_NAME;

import org.ebsdimage.core.exp.ops.indexing.op.KriegerLassen1994;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;

/**
 * XML saver for a <code>KriegerLassen1994</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class KriegerLassen1994XmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(KriegerLassen1994)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((KriegerLassen1994) obj);
    }



    /**
     * Saves a <code>KriegerLassen1994</code> operation to an XML
     * <code>Element</code>.
     * 
     * @param op
     *            a <code>KriegerLassen1994</code> operation
     * @return an XML <code>Element</code>
     */
    public Element save(KriegerLassen1994 op) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_MAX_INDEX, Integer.toString(op.maxIndex));
        element.setAttribute(ATTR_SCATTER_TYPE, op.scatterType.toString());

        return element;
    }

}
