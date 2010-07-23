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
package org.ebsdimage.io.exp.ops.pattern.op;

import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_SIZE;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_START_INDEX;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternSmpLoaderXmlTags.ATTR_FILEDIR;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternSmpLoaderXmlTags.ATTR_FILENAME;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternSmpLoaderXmlTags.TAG_NAME;

import org.ebsdimage.core.exp.ops.pattern.op.PatternSmpLoader;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;

/**
 * XML saver for a <code>PatternSmpLoader</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class PatternSmpLoaderXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(PatternSmpLoader)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((PatternSmpLoader) obj);
    }



    /**
     * Saves a <code>PatternSmpLoader</code> operation to an XML
     * <code>Element</code>.
     * 
     * @param op
     *            a <code>PatternSmpLoader</code> operation
     * @return an XML <code>Element</code>
     */
    public Element save(PatternSmpLoader op) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_START_INDEX, Integer.toString(op.startIndex));
        element.setAttribute(ATTR_SIZE, Integer.toString(op.size));
        element.setAttribute(ATTR_FILEDIR, op.filedir);
        element.setAttribute(ATTR_FILENAME, op.filename);

        return element;
    }

}
